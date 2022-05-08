const express = require("express");
const cors = require("cors");
const https = require("https");
const fs = require("fs");
const swaggerUi = require("swagger-ui-express");
const bodyParser = require("body-parser");
const swaggerDocument = require("./openapi.json");
const jwt = require("jsonwebtoken");
require("dotenv").config();

// the authentication model class, uses mongo db
// to authenticate a client
const model = require("./models/auth_model.js");

// Initialize express and use the bodyparser for
// getting http post body data.
const app = express();
app.use(cors());
app.use(bodyParser.json());

app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

const amqp = require("amqplib/callback_api");

function sendMsg(queue, msg) {
  amqp.connect("amqp://localhost", function (error0, connection) {
    if (error0) {
      throw error0;
    }
    connection.createChannel(function (error1, channel) {
      if (error1) {
        throw error1;
      }

      channel.assertQueue(queue, {
        durable: true,
      });

      channel.sendToQueue(queue, Buffer.from(msg));

      console.log(" [x] Sent %s", msg);
    });
    setTimeout(function () {
      connection.close();
    }, 500);
  });
}

// just a generic home page for a web portal if desired.
app.get("/", function (req, res) {
  res.send("TokenServer");
});

app.get("/accounts", async function (req, res) {
  try {
    const accounts = await model.getAll();
    res.send(accounts);
  } catch (err) {
    // error handling goes here
    res.send(err);
  }
});

// an API route for the authorization/authentication step
app.post("/authorize", function (req, res) {
  // get the client_id and secret from the client application
  const id = req.body.id;
  const secret = req.body.secret;

  // authorize with the model, using promises
  model
    .authorize(+id, secret)
    .then(function (docs) {
      let token_response = {};
      token_response.success = false;

      // if we have a user with the passed client_id and client_secret
      // then create a token
      if (docs && docs.length > 0) {
        token_response.id = docs[0].id;
        token_response.success = true;
        // sign the token and add it to the response object
        let token = jwt.sign(token_response, process.env.SECRET);
        token_response.token = token;
      }

      // return the token back to the client application
      res.send(token_response);
    })
    .catch(function (err) {
      // error handling goes here
      res.send(err);
    });
});

// this is an API call to verify the access_token when it's passed
app.post("/verify", function (req, res) {
  // get the token that was passed in the app.
  // might make more sense to put this in the header
  const token = req.headers.authorization;
  // decode the token to verify
  const decoded = jwt.verify(
    token,
    process.env.SECRET,
    function (err, decoded) {
      if (err) {
        // if there is an error, the token is not valid!
        res.send({ success: false });
      } else {
        // if no error, send the decoded token to the client with
        // authorization metadata payload
        res.send(decoded);
      }
    }
  );
});

app.post("/register", async function (req, res) {
  const secret = req.body.secret;
  const role = req.body.role;
  const name = req.body.name;

  try {
    const registered = await model.register(secret, role, name);

    const msg = {
      eventName: "User.Created",
      data: {
        id: registered.id,
        name,
        role,
      },
    };

    sendMsg("accounts", JSON.stringify(msg));
    return res.status(201).json(registered);
  } catch (err) {
    res.send(err);
  }
});

// file location of private key
const privateKey = fs.readFileSync("example.key");

// file location of SSL cert
const certificate = fs.readFileSync("example.crt");

const server_config = {
  key: privateKey,
  cert: certificate,
};

const https_server = https
  .createServer(server_config, app)
  .listen(2600, function (err) {
    console.log("Node.js Express HTTPS Server Listening on Port 2600");
  });

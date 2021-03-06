const DataAccess = function () {
  (this.MongoClient = require("mongodb").MongoClient),
    (assert = require("assert"));
  this.Mongo = require("mongodb");
  this.DBConnectionString = process.env.MONGO_CONNECTION.replace(/\\/g, "");
};

function randomInteger(from, to) {
  return from + Math.floor(Math.random() * (to - from));
}

DataAccess.prototype.MakeMongoId = function (id) {
  return this.Mongo.ObjectID(id);
};

DataAccess.prototype.GetEntities = function (dbName, collectionName, query) {
  const that = this;
  if (!query) {
    query = {};
  }

  return new Promise(function (fulfill, reject) {
    that.MongoClient.connect(that.DBConnectionString)
      .then(function (db) {
        const database = db.db(dbName);
        const collection = database.collection(collectionName);
        collection.find(query).toArray(function (err, docs) {
          db.close();
          if (err) {
            reject(err);
          } else {
            fulfill(docs);
          }
        });
      })
      .catch(function (err) {
        reject(err);
      });
  });
};

DataAccess.prototype.InsertEntity = function (entity, dbName, collectionName) {
  const that = this;

  return new Promise(function (fulfill, reject) {
    that.MongoClient.connect(that.DBConnectionString)
      .then(function (db) {
        const database = db.db(dbName);
        const collection = database.collection(collectionName);
        collection.insertOne(
          { id: randomInteger(1, Number.MAX_SAFE_INTEGER), ...entity },
          function (err, docs) {
            db.close();
            if (err) {
              reject(err);
            } else {
              fulfill(docs.ops[0]);
            }
          }
        );
      })
      .catch(function (err) {
        reject(err);
      });
  });
};

DataAccess.prototype.DeleteById = function (id, dbName, collectionName) {
  var that = this;
  return new Promise(function (fulfill, reject) {
    that.MongoClient.connect(that.DBConnectionString)
      .then(function (db) {
        var database = db.db(dbName);
        var collection = database.collection(collectionName);
        collection.deleteOne(
          { _id: new that.Mongo.ObjectID(id) },
          function (err, result) {
            db.close();
            if (err) {
              reject(err);
            } else {
              fulfill(result);
            }
          }
        );
      })
      .catch(function (err) {
        reject(err);
      });
  });
};

DataAccess.prototype.DeleteEntity = function (
  id,
  collectionName,
  databaseName,
  res
) {
  var that = this;

  //var outerResponse = res;
  this.MongoClient.connect(this.DBConnectionString, function (err, db) {
    assert.equal(null, err);

    const database = db.db(databaseName);
    const collection = database.collection(collectionName);
    //console.log(id);
    collection.deleteOne(
      { _id: new that.Mongo.ObjectID(id) },
      function (err, results) {
        if (err) {
          res.send("error", err);
          //throw err;
        }

        // sometimes there is no HTTP response to send because this may have been an internal api call.
        if (res) {
          res.send(results);
        }
      }
    );
    db.close();
  });
};

module.exports = new DataAccess();

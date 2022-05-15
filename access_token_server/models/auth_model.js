const dataAccess = require("./dataAccess.js");
const Model = function () {};

Model.prototype.authorize = function (id, secret) {
  return dataAccess.GetEntities("token_server", "clients", {
    id,
    secret,
  });
};

Model.prototype.register = function (secret, role, name) {
  return dataAccess.InsertEntity(
    { secret, role, name },
    "token_server",
    "clients"
  );
};

Model.prototype.getAll = function () {
  return dataAccess.GetEntities("token_server", "clients", {});
};

module.exports = new Model();

'use strict';
module.exports = function(sequelize, DataTypes) {
  var Friendship = sequelize.define('Friendship', {
    accepted: {
        type: DataTypes.BOOLEAN,
        defaultValue: false,
        allowNull: false
    }
   }, {
  paranoid: true,
  underscored: true,
  freezeTableName: true,
    classMethods: {
      associate: function (models) {

      }
    },
    //Methode pour l'instance d'un Utilisateur
    instanceMethods: {
    }
  });
  return Friendship;
};

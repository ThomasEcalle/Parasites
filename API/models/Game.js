'use strict';

module.exports = function(sequelize, DataTypes) {
  var Game = sequelize.define('Game', {
    id: {
         type: DataTypes.BIGINT,
         primaryKey: true,
         autoIncrement: true
    },
	nb_player: {
		type: DataTypes.BIGINT
	},
	size: {
		type: DataTypes.BIGINT
	}
   }, {
  paranoid: true,
  underscored: true,
  freezeTableName: true,
    classMethods: {
      associate: function (models) {
		  Game.belongsTo(models.User);
      }
    },
    //Methode pour l'instance d'un Utilisateur
    instanceMethods: {
    }
  });
  return Game;
};

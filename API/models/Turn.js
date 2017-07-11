'use strict';

module.exports = function(sequelize, DataTypes) {
  var Turn = sequelize.define('Turn', {
    id: {
         type: DataTypes.BIGINT,
         primaryKey: true,
         autoIncrement: true
    },
	board: {
		type: DataTypes.TEXT
	}
   }, {
  paranoid: true,
  underscored: true,
  freezeTableName: true,
    classMethods: {
      associate: function (models) {
		  Turn.belongsTo(models.User);
		  Turn.belongsTo(models.Game);
      }
    },
    //Methode pour l'instance d'un Utilisateur
    instanceMethods: {
    }
  });
  return Turn;
};

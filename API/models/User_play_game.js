'use strict';

module.exports = function(sequelize, DataTypes) {
  var User_play_game = sequelize.define('User_play_game', {
    
   }, {
  paranoid: true,
  underscored: true,
  freezeTableName: true,
    classMethods: {
      associate: function (models) {
		models.User.belongsToMany(models.Game, { through: User_play_game });
		models.Game.belongsToMany(models.User, { through: User_play_game });
      }
    },
	
    instanceMethods: {
    }
  });
  
  return User_play_game;
};

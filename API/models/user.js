'use strict';
module.exports = function(sequelize, DataTypes) {
  var User = sequelize.define('User', {
    id: {
         type: DataTypes.BIGINT,
         primaryKey: true,
         autoIncrement: true
    },
     email: {
       type: DataTypes.STRING,
       allowNull: false,
       validate: {
         isEmail: {
           args: true,
           msg: "Wrong email adress"
         }
       }
     },
     lastname: {
         type: DataTypes.STRING,
         allowNull: false,
         validate: {
           is: {
             args: ["^[a-z]+$",'i'],
             msg: 'Your last name contains something else than letters'
           }
         }
     },
     firstname: {
         type: DataTypes.STRING,
         allowNull: false,
         validate: {
           is: {
             args: ["^[a-z]+$",'i'],
             msg: 'Your first name contains something else than letters'
           }
         }
     },
     pseudo: {
         type: DataTypes.STRING,
         allowNull: false,
        //  unique: {
        //       args: true,
        //       msg: 'pseudo already exist !'
        //   }
     },
     password: {
       type: DataTypes.STRING,
       allowNull: false,
       validate: {
         is: {
           args: /^(?=.*[A-Z].*[A-Z])(?=.*[0-9].*[0-9])(?=.*[a-z].*[a-z].*[a-z]).{6,}$/i,
           msg: 'Your password must contain 2 Uppercase characters, 2 minor cases and 2 digits'
         }
       }
     },
     phone_number: {
       type: DataTypes.STRING,
       validate: {
         isNumeric: {
          args: true,
          msg: 'A phone number has to contain only numbers'
         }
       }
     },
     token_available: {
       type: DataTypes.STRING,
     }
   }, {
  paranoid: true,
  underscored: true,
  freezeTableName: true,
    classMethods: {
      associate: function (models) {
        User.belongsToMany(models.User, {as: 'Friends', through: models.Friendship});
      }
    },
    //Methode pour l'instance d'un Utilisateur
    instanceMethods: {
      responsify: function(){
        let result = {};
        result.id = this.id;
        result.pseudo = this.pseudo;
        return result;
      }
    }
  });
  return User;
};

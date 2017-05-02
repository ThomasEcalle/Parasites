/**
 * Created by Robin on 06/04/2017.
 */

var Fighter = function(positionX, positionY)
{
    Pawn.apply(this, [positionX, positionY]);
    this.val = 8;
}
Fighter.prototype = new Pawn();
Fighter.prototype.constructor = Fighter;
Fighter.prototype.getMoves = function()
{
    var moves = new Array();
    var x1 = this.x;
    var y1 = this.y;

    moves.push({x:x1-1 , y:y1})
    moves.push({x:x1+1 , y:y1})
    moves.push({x:x1 , y:y1-1})
    moves.push({x:x1 , y:y1+1})
    return moves;
}

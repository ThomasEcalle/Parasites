/**
 * Created by Robin on 06/04/2017.
 */

var Colony = function(positionX, positionY)
{
    Pawn.apply(this, [positionX, positionY]);
    this.val = 11;
}
Colony.prototype = new Pawn();
Colony.prototype.constructor = Colony;
Colony.prototype.getMoves = function()
{
    var moves = new Array();
    var x1 = this.x;
    var y1 = this.y;

    moves.push({x:x1-1 , y:y1})
    moves.push({x:x1+1 , y:y1})
    moves.push({x:x1 , y:y1-1})
    moves.push({x:x1 , y:y1+1})

    moves.push({x:x1-1 , y:y1-1})
    moves.push({x:x1-1 , y:y1+1})
    moves.push({x:x1+1 , y:y1+1})
    moves.push({x:x1+1 , y:y1-1})
    return moves;
}

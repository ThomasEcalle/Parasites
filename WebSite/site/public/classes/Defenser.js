/**
 * Created by Robin on 06/04/2017.
 */

var Defenser = function(positionX, positionY)
{
    Pawn.apply(this, [positionX, positionY]);
    this.val = 9;
}
Defenser.prototype = new Pawn();
Defenser.prototype.constructor = Defenser;
Defenser.prototype.getMoves = function()
{
    var moves = new Array();
    var x1 = this.x;
    var y1 = this.y;
    moves.push({x:x1+1 , y:y1})
    moves.push({x:x1+2 , y:y1})
    moves.push({x:x1-1 , y:y1})
    moves.push({x:x1-2 , y:y1})
    moves.push({x:x1 , y:y1+1})
    moves.push({x:x1 , y:y1+2})
    moves.push({x:x1 , y:y1-1})
    moves.push({x:x1 , y:y1-2})
    return moves;
}


var Queen = function(positionX, positionY)
{
    Pawn.apply(this, [positionX, positionY]);
    this.val = 7;

}
Queen.prototype = new Pawn();
Queen.prototype.constructor = Queen;
Queen.prototype.getMoves = function()
{
    var moves = new Array();
    var x1 = this.x;
    var y1 = this.y;

    moves.push({x:x1-1 , y:y1-1})
    moves.push({x:x1-1 , y:y1+1})
    moves.push({x:x1+1 , y:y1+1})
    moves.push({x:x1+1 , y:y1-1})
    moves.push({x:x1+1 , y:y1})
    moves.push({x:x1+2 , y:y1+1})
    moves.push({x:x1-2 , y:y1-1})
    moves.push({x:x1-2 , y:y1+1})
    moves.push({x:x1+2 , y:y1-1})
    moves.push({x:x1+2 , y:y1})
    moves.push({x:x1+3 , y:y1})
    moves.push({x:x1-1 , y:y1})
    moves.push({x:x1-2 , y:y1})
    moves.push({x:x1-3 , y:y1})
    moves.push({x:x1 , y:y1+1})
    moves.push({x:x1 , y:y1+2})
    moves.push({x:x1+1 , y:y1+2})
    moves.push({x:x1-1 , y:y1+2})
    moves.push({x:x1+1 , y:y1-2})
    moves.push({x:x1-1 , y:y1-2})
    moves.push({x:x1 , y:y1+3})
    moves.push({x:x1 , y:y1-1})
    moves.push({x:x1 , y:y1-2})
    moves.push({x:x1 , y:y1-3})
    return moves;
}
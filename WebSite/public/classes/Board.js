function Board(nom) {
var mapData = {
    "tileset" : "basique.png",
    "terrain" : [
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4 ,4 ,4 ,4 ,4 ,4 ,4 ,4  ,4  ,4  ,4  ,4  ,4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4],
        [4, 4, 4, 4, 4, 4, 4, 4, 4,  4,  4,  4,  4,  4,  4]
    ]
};

    // var mapData = JSON.parse(mapjson);
    this.tileset = new Tileset(mapData.tileset);
    this.terrain = mapData.terrain;

// Pour ajouter un personnage
}

Board.prototype.setFloor = function(x,y, val)
{
    this.terrain[y][x] = val;
}
Board.prototype.getFloor = function(x,y)
{
    //console.log(this.terrain[y][x]);
    return this.terrain[y][x];
}
Board.prototype.getHauteur = function()
{
    return this.terrain.length;
}

Board.prototype.getLargeur = function()
{
    return this.terrain[0].length;
}
Board.prototype.dessinerBoard = function(context)
{
    for(var i = 0, l = this.terrain.length ; i < l ; i++)
    {
        var ligne = this.terrain[i];
        var y = i * 32;
        for(var j = 0, k = ligne.length ; j < k ; j++)
        {
            this.tileset.dessinerTile(ligne[j], context, j * 32, y);
        }
    }


}

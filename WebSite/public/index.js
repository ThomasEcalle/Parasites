var ctx;
var pawn = new Queen(2,3);
var board;
var currx = -1;
var curry = -1;
var list;
var list2;
var ready = 1;
var interv1;
var prevv = 0;
var play = 0;
var coords;
var reineX;
var reineY;
function distanceFromCell(x,y,x1,y1)
{
	var dist = Math.abs(x-x1 + y-y1);
	// console.log("dist="+dist);
	return dist;
}
function tutorialIA()
{
	// console.log("toto");
	var randX = Math.floor((Math.random() * 14));
	var randY = Math.floor((Math.random() * 14));
	while (distanceFromCell(randX,randY,reineX,reineY) < 5)
	{
		randX = Math.floor((Math.random() * 14));
		randY = Math.floor((Math.random() * 14));
	}
	
	list.push({x:randX , y:randY})
	board.setFloor(Math.floor(randX), Math.floor(randY), 7);
	list2.push({x:randX , y:randY, z:7});
}
function showCases(cord)
{

    var x1 =  Math.floor(cord.x / 32);
    var y1 =  Math.floor(cord.y / 32);
    // console.log("x: "+ x1+" ,y: "+ y1)
    // console.log(pawn.val)
    var tmpPawn;
    if (pawn.val == 7)
        tmpPawn = new Queen(x1, y1);
    else if (pawn.val == 8)
        tmpPawn = new Fighter(x1, y1);
    var tab = tmpPawn.getMoves();
    // var tab = pawn.getMoves();
    // console.log(tab)
    resetBoard();
    afficherList(2);
    for (var i = 0; i < tab.length; ++i)
    {

        if (tab[i].x >= 0 && tab[i].x <= 14 && tab[i].y >= 0 && tab[i].y <= 14)
        {

            board.setFloor(tab[i].x, tab[i].y, 1)

        }
    }
    prevv = board.getFloor(x1,y1);
    board.setFloor(x1, y1, tmpPawn.val)
    return tab;

}
function getCoords(el,event) {
    var ox = -el.offsetLeft,
        oy = -el.offsetTop;
    while(el=el.offsetParent){
        ox += el.scrollLeft - el.offsetLeft;
        oy += el.scrollTop - el.offsetTop;
    }
    return {x:event.clientX + ox , y:event.clientY + oy};
}

window.onload = function() {
    list = new Array()
    list2 = new Array()
    //joueur.score = 100;
    board = new Board("2");
    var canvas = document.getElementById('canvas');
    ctx = canvas.getContext('2d');

    canvas.width  = board.getLargeur() * 32;
    canvas.height = board.getHauteur() * 32;

    board.dessinerBoard(ctx);
	 play=0;
    // speak("Bienvenue sur Parasite!","");
    // speak("Tout d'abord, placez votre reine sur le plateau","");
	  speak("Bienvenue sur Parasite!\n\n\nTout d'abord, placez votre reine sur le plateau","");
    setInterval(function() {
        board.dessinerBoard(ctx);
    }, 40);

    $("#canvas").click(function(e)
    {
		if (play == 0)
			return;
        coords = getCoords(this,e);
        var val = 0;



       if ((prevv == 2) || (prevv == 1) || (pawn.val == 7 && board.getFloor(Math.floor(coords.x / 32),Math.floor(coords.y / 32)) != 3))
        {
           // console.log("je MET un " + type + "en " + Math.floor(coords.x / 32) + "|" + Math.floor(coords.y / 32))
            //if (type == "attaquant")
            //{
            // console.log("pawn: ");
            // console.log(pawn);
            var tmp = showCases(coords);
            // console.log(tmp);
            for (var i = 0; i < tmp.length; ++i)
            {
                if (tmp[i].x >= 0 && tmp[i].x <= 14 && tmp[i].y >= 0 && tmp[i].y <= 14)
                {

                    list.push({x:tmp[i].x , y:tmp[i].y})
                    board.setFloor(Math.floor(coords.x / 32), Math.floor(coords.y / 32), pawn.val);
                    list2.push({x:Math.floor(coords.x / 32) , y:Math.floor(coords.y / 32), z:pawn.val});

                }


            }
            if (pawn.val == 7)
			{
				play = 0;
               // speak("Felicitation !","");
			   reineX = Math.floor(coords.x / 32);
			   reineY = Math.floor(coords.y / 32);
			    tutorialIA();
                speak("Felicitation !\nMaintenant placez un attaquant!","");
				
			}

            pawn = new Fighter(0,0);

        }
        // else{console.log(board.getFloor(Math.floor(coords.x / 32),Math.floor(coords.y / 32)))}


    })
    $("#debug").click(function(e)
    {
        printtab(board.terrain)


    })
    $("#canvas").mousemove(function(e)
    {

        var coords = getCoords(this,e);
        if (currx == Math.floor(coords.x / 32) && Math.floor(coords.y / 32) == curry)
        {
            return;
        }
        currx = Math.floor(coords.x / 32);
        curry = Math.floor(coords.y / 32);

		showCases(coords);


    });
}
function settype(str)
{
    // console.log(str);
    type = str;

}

function can()
{
    console.log('ttttttttttttttt')
}
function resetBoard()
{

    for (var i = 0; i < 15; ++i)
    {
        for (var j = 0; j < 15; ++j)
        {

            board.setFloor(i,j,4);
        }

    }
}

function afficherList(nb)
{
    var tab = list;
    var tab2 = list2;
    for (var i = 0; i < tab.length; ++i)
    {
        board.setFloor(tab[i].x, tab[i].y, nb)
    }
    for (var i = 0; i < tab2.length; ++i)
    {
        board.setFloor(tab2[i].x, tab2[i].y, tab2[i].z)
    }
}


function printtab(tab) // debug
{
    var string = "";
    for (var i = 0; i < 15; ++i)
    {
        for (var j = 0; j < 15; ++j)
        {
            string = string + tab[i][j] + " ";
            if (tab[i][j] < 10)
                string += "   ";
            else if (tab[i][j] < 100)
                string += "  ";
        }
        string += "\n";
    }
    console.log(string);
}
function speak(str, interval)
{

    if (ready == 1)
	{
		play=0;
        $('#annoncer').text("")
        var i = 0;
        ready = 0;
        var interv = setInterval(function() {
            // if (str.charAt(i) == "\n")
			// {
             //    sleep(2000);
             //    $('#annoncer').text("");
			// }

            var tmp = $('#annoncer').text();
			if (str.charAt(i) == "\n")
				$('#annoncer').text("")
			else
				$('#annoncer').text(tmp + str.charAt(i))
            ++i;
            if (i >= str.length)
			{
				ready=1;
				if (interval == 'interv')
				{
					clearInterval(interv1);
					
				}
				play=1;
				clearInterval(interv);
				

			}

        }, 80);
	}
	else
	{
		if (interval != 'interv')
		{
            interv1 = setInterval(function() {
                speak(str,'interv');
            }, 4000);
		}

	}



}

function sleep(mil){
    var waitUntil = new Date().getTime() + mil;
    while(new Date().getTime() < waitUntil) true;
}
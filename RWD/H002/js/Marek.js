/*globals $:false*/
var counter = document.getElementById("countId");
var count = 0;
$("#sparkId").hide();

var maxHeight = $(window).height() - $(".crabs").height();
var maxWidth = $(window).width() - $(".crabs").width();

var keyup = true;
var animDone = true;
var maxHeight = $(window).height() - $(".crabs").height();
var maxWidth = $(window).width() - $(".crabs").width();
$(document).keydown(function (e) {
    if (keyup && animDone){
        var left = $("#crabId").offset().left;
        var left2 = $("#crabId2").offset().left;
        var left3 = $("#crabId3").offset().left;
        if(e.which == 37 && ( left>20 || left2>20 || left3>20)){
            animDone = false
            $(".crabs").animate({
                left: "-=50"
            }, "fast", function(){animDone = true});
            
            keyup = false
        }
        if(e.which == 39 && (left<maxWidth-50 && left2<maxWidth-50 &&left3<maxWidth-50)){
            animDone = false
            $(".crabs").animate({
                left: "+=50"
            }, "fast", function(){animDone = true});
            keyup = false
        }
        if(e.which == 38){
            animDone = false
            $(".crabs").animate({
                bottom: "+=50"
            }).animate({
                bottom: "0"
            }, "fast", checkCollisions);
            keyup = false
        }
    }
    
});

$(document).keyup(function(e){
    if( [37, 38, 39].includes(e.which) ){
        keyup = true;
    }
})

$("#sparkId").hide();


function getPositions(box) {
    var $box = $(box);
    var pos = $box.position();
    var width = $box.width();
    var height = $box.height();
    return [[pos.left, pos.left + width], [pos.top, pos.top + height]];
}

function comparePositions(p1, p2) {
    var x1 = p1[0] < p2[0] ? p1 : p2;
    var x2 = p1[0] < p2[0] ? p2 : p1;
    return x1[1] > x2[0] || x1[0] === x2[0] ? true : false;
}

function randomNumber(param){
    return Math.floor(Math.random()*(param+1));
}

function checkCollisions() {
    var box = $("#coinId");
    var pos = getPositions(box);
    var position = box.offset();
    var pos2 = getPositions(this);
    var horizontalMatch = comparePositions(pos[0], pos2[0]);
    var verticalMatch = comparePositions(pos[1], pos2[1]);
    var match = horizontalMatch && verticalMatch;
    var x = randomNumber(1000);
    var numberW = $(window).width() - $(".crabs").width();
    var numberH = $(window).height() - $(".crabs").height();
    
    if (match) {
        $("#sparkId").show(0).fadeOut(1000).css({left:position.left+35})
        $("#coinId").hide(0).css({left:x}).fadeIn(1000)
        count = count + 1;
        counter.innerHTML = count + " doubloons";
                }
    if(count==5)
        {
            $("#crabId").fadeOut(500);
            $("#crabId2").fadeIn(1000);
        }
    if(count==10)
        {
            $("#crabId2").fadeOut(500);
            $("#crabId3").fadeIn(1000);
        }
    animDone = true;
                }

counter.innerHTML = "Try collecting some coins using your arrow keys!";




/*globals $:false*/
//variables
const bubbles = $(".bubbleClass");


//functionality
//let them float
loopArray(bubbles, bubble => floatBubble(bubble));
displayBubbles(bubbles);

//Bubble click listener
bubbles.on("click", function (e) {
    e.stopPropagation();
    $(this)
        .stop(true)
        .attr("src", function () {
            if ($(this).hasClass("bubble1")) {
                return "images/bubblePop1.png";
            }
            else {
                return "images/bubblePop2.png";
            }
        })
        .fadeOut(300, function () {
            floatBubble(this);
        });
});
// FIRST ATTEMPT
// Works, however laggy
// bubbles.forEach(function (bubble) {
//     $(bubble).on("click", function () {
//         $(bubble).attr("src", function () {
//             setTimeout(function () {
//                 $(bubble).attr("src", function () {
//                     return $(bubble).hasClass("bubble1") ? "images/bubble.png" : "images/bubble2.png";
//                 });
//                 spawn(bubble);
//                 $(bubble).removeClass("pop");
//                 floatBubble(bubble);
//             }, 100);
//             if ($(bubble).hasClass("bubble1")) {
//                 return "images/bubblePop1.png";
//             }
//             else {
//                 return "images/bubblePop2.png";
//             }
//         });
//         $(bubble)
//             .stop()
//             .delay(1000)
//             .fadeOut(80)
//             .stop(1)
//             .addClass("pop");
//     })
// });

//function declarations
function spawn(bubble) {
    let size = Math.floor(Math.random() * 100 + 50);
    let width = $(window).width() - size;
    let bubPos = {
        x: Math.floor(Math.random() * width),
        y: $(document).height() + 150
    };
    $(bubble)
        .show()
        .offset({top: bubPos.y, left: bubPos.x})
        .css({width: size, height: size});
}

function displayBubbles(bubbleArr) {
    for (let i = 0; i < bubbleArr.length; i++) {
        $(bubbleArr[i]).css({display: "block"});
    }
}

function loopArray(array, loopFunction) {
    for (let i = 0; i < array.length; i++) {
        loopFunction(array[i]);
    }
}

function floatBubble(bubble) {
    let speed = Math.floor(Math.random() * 5000 + 6000);
    spawn(bubble);
    $(bubble)
        .stop(1)
        .attr("src", () => {
            return $(bubble).hasClass("bubble1") ? "images/bubble.png" : "images/bubble2.png";
        })
        .animate({top: "-110"}, speed, "linear", () => {
            floatBubble(bubble);
        })
}
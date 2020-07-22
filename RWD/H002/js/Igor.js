function run(element, vOffset = 500, hOffset = 500){
    var top = $(element).offset().top;
    var left = $(element).offset().left;
    var maxHeight = $(window).height() - $(element).height();
    var maxWidth = $(window).width() - $(element).width();

    var topOffset = (vOffset * plusMinus());
    var leftOffset = (hOffset * plusMinus());

    var newTop = Math.abs(top + topOffset)
    var newLeft = Math.abs(left + leftOffset)

    if (newLeft < left){
        element.addClass('flipped')
    }else{
        element.removeClass('flipped')
    }

    if (newTop > maxHeight){newTop = maxHeight}
    if (newLeft > maxWidth){newLeft = maxWidth}


    $(element).animate( {top:newTop, left:newLeft}, Math.random() * 1000 + 500, function(){ swim($(element, 200, 300)) } )
}
$("#fish2Id").mouseenter(function ()
{
    $(this).stop(true);
    run($(this), 700, 500);
});
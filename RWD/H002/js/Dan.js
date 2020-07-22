var redFish = $('#redFish')

function plusMinus(){
    return [1, -1][Math.round(Math.random())]
}

function swim(element, vOffset = 200, hOffset = 300){
    var top = $(element).offset().top;
    var left = $(element).offset().left;
    var maxHeight = $(window).height() - $(element).height();
    var maxWidth = $(window).width() - $(element).width();
    
    var topOffset = (Math.random() * vOffset * plusMinus());
    var leftOffset = (Math.random() * hOffset * plusMinus());
      
    var newTop = Math.abs(top + topOffset)
    var newLeft = Math.abs(left + leftOffset)
    
    if (newLeft < left){
        element.addClass('flipped')
    }else{
        element.removeClass('flipped')
    }
    
    if (newTop > maxHeight){newTop = maxHeight}
    if (newLeft > maxWidth){newLeft = maxWidth}
    
    
    $(element).animate( {top:newTop, left:newLeft}, Math.random() * 1000 + 500, function(){ swim($(element, vOffset, hOffset)) } )
}

swim(redFish)
swim($('#fish2Id'))

function animateRotate(elem, degree = -360, step = 2, callback) {
    
    nowDegree = 0
    flipProp = ''
    
    isFlipped = elem.hasClass('flipped');
    if (isFlipped){ flipProp = 'scale(-1, 1)'; degree = degree * -1; }
    
    sign = degree > 0 ? 1 : -1

    var interval = setInterval(function(){
        nowDegree = nowDegree + ( step * sign );
        
        elem.css({transform : `rotate(${nowDegree}deg) ${flipProp}`})
        
        if (sign < 0){
            if (nowDegree <= degree){
                elem.css({transform : ``})
                clearInterval(interval)
                callback()
            }
        }else if(nowDegree >= degree){
            elem.css({transform : ``})
            clearInterval(interval) 
            callback()
        }
        
    }, 1000/60)
}

$(redFish).on("click", function(e){
    e.stopPropagation()
    
    $(this).stop(true)
    animateRotate(redFish, -360, 4, function(){swim($(redFish))})
    
})

$(window).on("click", function(e){
    var fishTop = redFish.offset().top;
    var fishLeft = redFish.offset().left;
    
    var top = e.pageY - 75;
    var left = e.pageX;
    
    redFish.stop(true);
    
    if (left < fishLeft){
        redFish.addClass('flipped')
    }else{
        redFish.removeClass('flipped')
        left -= 200;
    }
    $(redFish).animate( {top:top, left:left}, Math.random() * 1000 + 500, function(){ 
        redFish.delay(300)
        swim(redFish) 
    })
})
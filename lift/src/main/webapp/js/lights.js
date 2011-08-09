function d2h(dec) { return dec.toString(16);}
function h2d(hex) { return parseInt(hex,16);}
function rgb2h(r,g,b) { return [d2h(r),d2h(g),d2h(b)];}
function h2rgb(h,e,x) {return [h2d(h),h2d(e),h2d(x)];}

function cssColor2rgb(color) {
    if(color.indexOf('rgb')<=-1) {
        return h2rgb(color.substring(1,3),color.substring(3,5),color.substring(5,7));
    }

    var color=color.substring(1,4);
    increase=color.substring(color.length-1);
    return increase.split(',');
}

function animateColor(elm,begin,end, duration, fps) {
    if(!duration) duration = 1000;
    if(!fps) fps = 20;
    duration=parseFloat(duration);
    fps=parseFloat(fps);
    var interval    = Math.ceil(1000/fps);
    var totalframes = Math.ceil(duration/interval);

    for(i=1;i <= totalframes;i++) {
         (function() {
            var frame=i;
            var b = cssColor2rgb(begin);
            var e  = cssColor2rgb(end);
            var change0=e[0]-b[0];
            var change1=e[1]-b[1];
            var change2=e[2]-b[2];

            function generalProperty() {
              var increase0=ease(frame, b[0], change0, totalframes);
              var increase1=ease(frame, b[1], change1, totalframes);
              var increase2=ease(frame, b[2], change2, totalframes);

              elm.style['backgroundColor']  = 'rgb('+parseInt(increase0)+','+parseInt(increase1)+','+parseInt(increase2)+')';
            }
            timer = setTimeout(generalProperty,interval*frame);
         })();
    }
}
function ease(frame,begin,change,totalframes) {
//linear bezier :
       return begin+((begin+change)-begin)*(frame/totalframes);
}

function changeColor() {
    var start=document.getElementById('colorStart').value;
    var end=document.getElementById('endStart').value;
    animateColor(document.getElementById('squareColor'),start,end,3000,20);
}
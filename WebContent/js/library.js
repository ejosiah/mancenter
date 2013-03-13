Function.prototype.repeat = function(interval){
	return setInterval(this, interval);
};

Function.prototype.delay = function(timeout){
	return setTimeout(this, timeout);
};

String.prototype.capitalize = function(){
	var val = this;
	return val[0].toUpperCase() + val.substring(1);
}
Array.prototype.each = Array.prototype.forEach;

$ = document.getElementById;
$.get = function(clsName){
	return document.getElementsByClassName(clsName);
};

$.create = function(elmName) { return document.createElement(elmName); };
$.text = function(value){ return document.createTextNode(value); };

function memorize(f){
	var cache = {};
	
	return function(){
		var args = Array.prototype.slice.call(arguments);
		var key = args.length + args.join('');
		if(cache[key]){
			return cache[key];
		}else{
			return key = f.apply(this, args);
		}
	};
}
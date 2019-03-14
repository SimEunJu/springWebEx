Handlebars.registerHelper("dateFormat", function(date){
		
		if(date === null) return;
			return date.year+"-"+(date.monthValue<10 ? "0"+date.monthValue : date.monthValue)
				+"-"+(date.dayOfMonth<10 ? "0"+date.dayOfMonth : date.dayOfMonth)
				+" "+(date.hour<10 ? "0"+date.hour : date.hour)
				+":"+(date.minute<10 ? "0"+date.minute : date.minute);
		});
		Handlebars.registerHelper("for", function(start, end, block){
			let acc = "";
			for(let i=start; i<=end; i++){
				acc += block.fn(i);
			}
			return acc;
		});
		
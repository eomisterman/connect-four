// index.js
// emilio ovalles-misterman
// assignment 1



const howFalse = function howFalse(a) {
    // iterate through each value in a
    // test for falsy-ness
    	// check dict for presence if true, increment. if false, add
        // to dict with value 1
    // return object

    const falsyDict = {};

    a.forEach(function(value) {
        // if falsy
    	if(!value) {
            // new entry
            if(falsyDict[value] === undefined) {
                falsyDict[value] = 0;
            }
            // increment value
    		falsyDict[value] = falsyDict[value] + 1;
    	}
    });

    console.log(falsyDict);
}



const myMin = function myMin() {
    let i;
    let _min;

    for(i=0; i < arguments.length; i++) {
        if(i===0) {
            _min = arguments[i];
        }
        else {
            if(arguments[i] < _min) {
                _min = arguments[i];
            }
        }
    }

    return _min;
}


console.log([5, 3 ,5 ,9, 2, 1].reduce(function reduceMax(accumulator, current) {
    // if new value is greater, return value -> will be accumulator for next
    //reduce call. else, accumulator is returned
    if(current > accumulator) {
        return current;
    } else {
        return accumulator;
    }
}));



const largestCommonSubstring = function largestCommonSubstring(str1, str2) {
    let arr = [];

    // initialize 2d array with values
    for(let i=0; i<=str1.length; i++) {
        arr.push([]);

        for(let k=0; k<=str2.length; k++) {
            let current = 0;
            if(i === 0 || k === 0) {
                current = 0;
            } else if(str1.charAt(i-1) === str2.charAt(k-1)) {
                current = arr[i-1][k-1] + 1;
            } else {
                current = Math.max(arr[i-1][k], arr[i][k-1]);
            }
            arr[i].push(current);
        }
    }

    let h = str1.length;
    let w = str2.length;
    let result = "";

    // work way back through array to find lcs
    // used link below as reference for lcs algorithm
    // https://gist.github.com/alexishacks/725df6db4432cd29cd43
    while(arr[h][w] > 0) {
        if(str1.charAt(h-1) === str2.charAt(w-1) &&
         (arr[h-1][w-1]+1 === arr[h][w])) 
        {
            result = str1.charAt(h-1) + result;
            h--;
            w--;
        } 
        else if(arr[h-1][w] > arr[h][w-1]) 
        {
            h--;
        } 
        else 
        {
            w--;
        }
    }
    return result;

}



function forDemo(_iterable) {
    // formatting
    console.log("INPUT:");
    console.log(_iterable);
    console.log("\n");

    console.log("*** For-in loop beginning ***");
    for(let i in _iterable) {
        console.log("      Current value: " + i);
    }
    console.log("*** End of for-in loop ***")

    console.log("\n");

    console.log("*** For-of loop beginning ***");
    for(let k of _iterable) {
        console.log("      Current value: " + k);
    }
    console.log("*** End of for-of loop ***")

}



























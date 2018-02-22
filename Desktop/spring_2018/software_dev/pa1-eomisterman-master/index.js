// TODO: Add your JavaScript functions



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

howFalse([5, 0, 42, 'dalek', 3-3, 7, Math.sqrt(-1)]);



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

    console.log(_min);
}

myMin(5,4,6,2,8,1);



console.log([5, 3 ,5 ,9, 2, 1].reduce(function reduceMax(accumulator, current) {
    if(current > accumulator) {
        return current;
    } else {
        return accumulator;
    }
}));



const createArray = function createArray(_array) {
    if(arguments.length < 1 || arguments.length > 2) {
        return -1;
    }
    else if(arguments.length === 1) {
        return new Array(arguments[0]);
    }
    else {
        const ddarr = new Array(arguments[0]);
        let i;
        for(i = 0; i<ddarr.length; i++) {
            ddarr[i] = new Array(arguments[1]);
        }
        return ddarr;
    }
}

console.log(createArray(3));
const test = createArray(3);
for( let i=0; i<test.length; i++) {
    test[i] = i;
}
console.log(test);
































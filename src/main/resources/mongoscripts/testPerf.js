var yesterday = new Date();
yesterday.setDate(yesterday.getDate() - 1);
var yesterdayString = yesterday.toISOString();

var lastMonth = new Date();
lastMonth.setDate(lastMonth.getDate() - 30);
var lastMonthString = lastMonth.toISOString();

var n = 1000;

var sumTime = 0;
var sumTimeMonth = 0;

for (i = 0; i < n; i++) {

    // Test aggregate query for day graph
    var start = new Date().getTime();
    var cursor = db.temperatures.aggregate([
        {$match:{"dateTime":{$gte:ISODate(yesterdayString)}, "show":{$ne:false}}},
        {$sort:{"dateTime":1}},
        {$group:{"_id":"$nameDevice","temperatures":{$push:{"date":"$dateTime","temperature":"$temperature"}}}}
    ]);
    var end = new Date().getTime();
    sumTime += (end - start);

    // Test aggregate query for month graph
    var start = new Date().getTime();
    var cursor = db.temperatures.aggregate([
        {$match:{dateTime:{$gte:ISODate(lastMonthString)}, show:{$ne:false} }},
        {$project:{nameDevice:1,temperature:1,date:{$dateToString:{format:"%Y-%m-%d", date:"$dateTime"}}}},
        {$group:{_id:{nameDevice:"$nameDevice", date:"$date"}, min:{$min:"$temperature"}, max:{$max:"$temperature"}, moy:{$avg:"$temperature"}}},
        {$sort:{"_id.date":1}},
        {$group:{_id:"$_id.nameDevice", points:{$push:{date:"$_id.date", min:"$min", max:"$max", moy:"$moy"}}}}
    ]);
    var end = new Date().getTime();
    sumTimeMonth += (end - start);
}

print("Day - Average time : " + (sumTime / n) + "ms");
print("Month - Average time : " + (sumTimeMonth / n) + "ms");


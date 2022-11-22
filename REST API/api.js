const http = require("http");
const axios = require("axios");
const { exit } = require("process");
const { getEnvironmentData } = require("worker_threads");
const { getLyrics } = require("genius-lyrics-api");
async function getMoviePrompt(){
    var wordRequirement = 500;
    var prompt ="";
    while (prompt.split(" ").length < wordRequirement){
        var randIndex = Math.floor(Math.random() * 19);
        var randPage = String(Math.floor(Math.random()*499)+1);
        request = "https://api.themoviedb.org/3/discover/movie?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&sort_by=popularity.desc&certification=PG&include_adult=false&include_video=false&page="+randPage+"&with_watch_monetization_types=flatrate";
        await axios.get(request) //Random Page for each request to guarantee different prompts. 
        .then (result => {
            prompt += result.data.results[randIndex].overview+" ";
        })
        .catch(error =>{})
    }
    prompt=prompt.replace(/[^a-zA-Z0-9 .]/gi,''); //some prompts have non ascii characters. Need to clean the data. 
    prompt=prompt.slice(0,-1); //remove space at end of prompt. 
    return prompt;
}
async function getLyricsPrompt(){
    var wordRequirement = 500;
    var prompt = "";
    var songs = ["https://genius.com/Elton-john-im-still-standing-lyrics","https://genius.com/Elton-john-bennie-and-the-jets-lyrics","https://genius.com/Elton-john-funeral-for-a-friend-love-lies-bleeding-lyrics","https://genius.com/Elvis-presley-trouble-guitar-man-nbc-tv-special-lyrics","https://genius.com/Elvis-presley-lawdy-miss-clawdy-lyrics","https://genius.com/Elvis-presley-baby-what-you-want-me-to-do-lyrics","https://genius.com/Elvis-presley-heartbreak-hotel-lyrics"];
    while (prompt.split(" ").length < wordRequirement){
        var randIndex = Math.floor(Math.random() * 7);
        await getLyrics(songs[randIndex])
        .then((lyrics) => {
            prompt+=lyrics+" ";
        });
    }
    prompt=prompt.replace(/[\n]/gi," ");
    prompt=prompt.replace(/[^a-zA-Z0-9 .]/gi,''); //some prompts have non ascii characters. Need to clean the data. 
    prompt=prompt.slice(0,-1); //remove space at end of prompt. 
    return prompt;   
}
async function getPoetryPrompt(){
    var wordRequirement = 500;
    var prompt = "";
    var poets = ["shakespeare","poe","whitman","milton","blake","wilde"];
    while (prompt.split(" ").length < wordRequirement){
        var randPoet = Math.floor(Math.random()*5);
        var randPoem = Math.floor(Math.random()*7);
        var request = "https://poetrydb.org/author/"+poets[randPoet]+"/lines";
        await axios.get(request)
        .then (result =>{
            var j=0;
            data = result.data[randPoem].lines;
            while (j < data.length && prompt.split(" ").length < wordRequirement){
                prompt+=result.data[randPoem].lines[j]+" ";
                j+=1;
            }
        })
        .catch(error =>{})
    }
    prompt=prompt.replace(/[^a-zA-Z0-9 .]/gi,'');
    prompt=prompt.replace(/  +/g, ' ');
    prompt=prompt.slice(0,-1); //remove space at end of prompt. 
    return prompt;
}
const server = http.createServer((req,res) => {
    async function getData(req){
        if(req=="/"){
            res.write("Welcome To The Fast Thumbs API");
            res.end();
        }
        if (req === '/api/getMoviePrompt'){
            var prompt = await getMoviePrompt();
            var words = prompt.split(" ").length;
            var json = {"prompt": prompt,
                        "words": words};
            res.write(JSON.stringify(json));
            res.end();
        } 
        if (req === '/api/getPoetryPrompt'){
            var prompt = await getPoetryPrompt();
            var words = prompt.split(" ").length;
            var json = {"prompt": prompt,
                        "words": words};
            res.write(JSON.stringify(json));
            res.end();
        }
        if (req === '/api/getLyricsPrompt'){
            var prompt = await getLyricsPrompt();
            var words = prompt.split(" ").length;
            var json = {"prompt": prompt,
                        "words": words};
            res.write(JSON.stringify(json));
            res.end();
        }
    }
    getData(req.url);
});

server.listen(80);

console.log('Listening on port 80');
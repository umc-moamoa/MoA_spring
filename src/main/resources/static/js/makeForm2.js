var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
var semiCategoryId;
var semiShortCount = 0;
var semiLongCount = 0;
var semiTitle;
var semiContent;
var semiDeadline;
var semiPostDetails = new Array();
var semiPostItems = new Array();
var semiPostFormat = new Array();
var elements = document.getElementsByClassName("surveyElement");
var questions = document.getElementsByClassName("Qinput");
var types = document.getElementsByClassName("Qtype");
var format;
var question;
var options;
var item1, item2;
var i;

function gotoMysurvey() {
    var link="../templates/mySurvey.html";
    location.href=link;
}

function gotoMain() {
    var link="../templates/main.html";
    location.href=link;
}

function setDeadline() {
    var deadline = document.getElementById("inputDate");
    deadline.value = new Date().toISOString().substring(0, 10);
    deadline.min = new Date().toISOString().substring(0, 10);
}

// 폼 데이터 정리 (보내기 위해)
function sortQuestion() {
    var Stype = document.getElementById("Stype");
    semiCategoryId = Stype.options[Stype.selectedIndex].value; 

    semiTitle = document.getElementById("inputTitle").value;
    semiContent = document.getElementById("inputExplain").value;

    semiDeadline = document.getElementById("inputDate").value;

    semiPostDetails = semiPostItems;
    for (i=0; i<elements.length; i++) {
        semiPostItems[i] = new Array(3);    // 2차원 배열
    }
    for (i=0; i<elements.length; i++) {     // elements[0]이 문항 1번.
        format = types[i].value;
        semiPostItems[i][0] = format;

        question = questions[i].value;
        semiPostItems[i][1] = question;

        semiPostFormat.splice(0, semiPostFormat.length);    // postFormat 비우고 추가.

        options = elements[i].children;
        if (types[i].value == 1 || types[i].value == 2) {
            for (j=1; j<options.length-1; j++) {
                item1 = options[j].children[1];
                if(item1) {
                    item2 = item1.value;
                    semiPostItems[i][1+j] = item2;
                }
            }
        } else if (types[i].value == 3) {
            semiPostFormat[i] = new Array(1);
            item1 = options[1].children[1]
            if(item1) {
                item2 = item1.value;
                semiPostItems[i][2] = item2;
            }
        } else if (types[i].value == 4) {
            semiPostFormat[i] = new Array(1);
            item1 = options[4]
            if(item1) {
                item2 = item1.value;
                semiPostItems[i][2] = item2;
            }
        }

        if (types[i].value == 4) {
            semiLongCount++;    
        }
        else {
            semiShortCount++;
        }
    }
    console.log(semiPostDetails);

    fetchMakeForm();
}

const fetchTokenCheck = () => {
    var requestOptions = {
        method: "Get",
        headers: {'REFRESH-TOKEN' : my_refresh, }
    };

    fetch(
        "http://seolmunzip.shop:9000/auth/refresh",
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult.code);
            localStorage.removeItem('x-access-token');
            localStorage.setItem('x-access-token', webResult.result);
            my_jwt = localStorage.getItem('x-access-token');
        })
        .catch((error) => console.log("error", error));
}

function fetchMakeForm() {
    const formItem = {
        "categoryId" : Number(semiCategoryId),
        "shortCount" : semiShortCount,
        "longCount" : semiLongCount,
        "title" : semiTitle,
        "content" : semiContent,
        "deadline" : semiDeadline,
        "postDetails" : semiPostDetails
    }

    console.log(JSON.stringify(formItem));

    fetch(`http://seolmunzip.shop:9000/posts` , {
        method: "POST",
        headers: {
            'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, 
            'Content-Type': 'application/json'            
        },
        body: JSON.stringify(formItem)
    })

    .then((response) => response.json())
    .then((response2) => {
        console.log(response2.code);
        if(response2.code == 1000) {
            gotoMysurvey();
        }
        if(response2.code == 2002) {
            fetchTokenCheck();
            const formItem = {
                "categoryId" : Number(semiCategoryId),
                "shortCount" : semiShortCount,
                "longCount" : semiLongCount,
                "title" : semiTitle,
                "content" : semiContent,
                "deadline" : semiDeadline,
                "postDetails" : semiPostDetails
            }
        
            console.log(JSON.stringify(formItem));
        
            fetch(`http://seolmunzip.shop:9000/posts` , {
                method: "POST",
                headers: {
                    'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, 
                    'Content-Type': 'application/json'            
                },
                body: JSON.stringify(formItem)
            })
        
            .then((response) => response.json())
            .then((response2) => {
                console.log(response2.code);
                if(response2.code == 1000) {
                    gotoMysurvey();
                }
            })
        }
        if (response2.code == 2013) {
            alert(response2.message);
            gotoMain();
        } else if (response2.code == 2018) {
            alert(response2.message);
            gotoMain();
        } else if (response2.code == 2019) {
            alert(response2.message);
            gotoMain();
        } 
    })
    .catch((error) => console.log("error", error))
}
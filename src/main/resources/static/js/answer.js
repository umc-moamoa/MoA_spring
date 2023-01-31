const $SurveyQuestion = document.querySelector("#SurveyQuestion");
var questions = document.getElementById("SurveyQuestion");
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
var semipostDetailResults = new Array();
var postDetailId;
var formats = new Array(); // 여기에 format 넣음. 
var checkboxes = new Array();
var postLength = 0;
var resultList = new Array();
var cnt = 0;

const rpostId = location.href.split('?')[1];
console.log(rpostId);

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


const fetchSuryeyIn = () => {
    var requestOptions = {
        method: "GET",
        headers: {'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, }
    };

    fetch(
        `http://seolmunzip.shop:9000/posts/${rpostId}`,
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult);
            console.log(webResult.code);
            if(webResult.code == 1000) {
                webResult.result.map(item => SurveyInTemplate(item));
                fetchAnswer();
            }
            if(webResult.code == 2002) {
                fetchTokenCheck();

                    fetch(
                        `http://seolmunzip.shop:9000/posts/${rpostId}`,
                        requestOptions
                    )
                        .then((response) => response.json())
                        .then((webResult) => {
                            console.log(webResult.code);
                            if(webResult.code == 1000) {
                                webResult.result.map(item => SurveyInTemplate(item));
                                fetchAnswer();
                            }
                        })
            }
            
        })
        .catch((error) => console.log("error", error));

}

fetchSuryeyIn();

var count = 0;
function SurveyInTemplate(data) {
    semipostDetailResults[count] = new Array(2);

    formats[count] = data.format;

    count++;
    var questionDiv = document.createElement("div");
    questionDiv.className = 'question';
    
    var inputDiv;
    // 객관식 - 라디오 버튼
    if(data.format == 1) {
        // 질문
        var q1Span = document.createElement("span");
        q1Span.className = 'Q1';
        q1Span.textContent = count + ".   " + `${data.question}`;

        questionDiv.appendChild(q1Span);
        questions.appendChild(questionDiv);

        // 답 - 라디오 버튼
        var rBtnDiv = document.createElement("div");
        rBtnDiv.className = 'radioBox'
        for(var j=0; j<data.items.length; j++) {
            var itemDiv = document.createElement("div");
            itemDiv.className = 'Qtype1';

            var inputDiv = document.createElement("input");
            inputDiv.type = 'radio';
            postDetailId = `${data.postDetailId}`;
            var pi = new Array();
            pi[0] = Number(postDetailId);
            semipostDetailResults[count-1][0] = pi;
            inputDiv.name = postDetailId;
            inputDiv.value = j;
            
            inputDiv.id = count * 10 + j + 1;
            console.log(inputDiv.id);

            var inputTextDiv = document.createElement("span");
            inputTextDiv.className = 'radioBtn';
            inputTextDiv.textContent = data.items[j];
            
            itemDiv.appendChild(inputDiv);
            itemDiv.appendChild(inputTextDiv);
            rBtnDiv.appendChild(itemDiv);
            questionDiv.appendChild(rBtnDiv);
        }

    }
    // 객관식 - 체크박스 버튼
    else if(data.format == 2) {
        // 질문
        var q1Span = document.createElement("span");
        q1Span.className = 'Q1';
        q1Span.textContent = count + ".   " + `${data.question}`;

        questionDiv.appendChild(q1Span);
        questions.appendChild(questionDiv);

        // 답 - 체크박스 버튼
        var rBtnDiv = document.createElement("div");
        rBtnDiv.className = 'checkBoxBox';
        postDetailId = `${data.postDetailId}`;
        semipostDetailResults[count-1][0] = Number(postDetailId);
        for(var j=0; j<data.items.length; j++) {
            var itemDiv = document.createElement("div");
            itemDiv.className = 'Qtype2';

            var inputDiv = document.createElement("input");
            inputDiv.type = 'checkbox';
            postDetailId = `${data.postDetailId}`;
            var pi = new Array();
            pi[0] = Number(postDetailId);
            semipostDetailResults[count-1][0] = pi;
            inputDiv.name = postDetailId;
            inputDiv.value = j;
            inputDiv.id = count * 10 + j + 1;
            console.log(inputDiv.id);

            var inputTextDiv = document.createElement("span");
            inputTextDiv.className = 'checkBoxBtn';
            inputTextDiv.textContent = data.items[j];
            
            itemDiv.appendChild(inputDiv);
            itemDiv.appendChild(inputTextDiv);
            rBtnDiv.appendChild(itemDiv);
            questionDiv.appendChild(rBtnDiv);
        }
    }
    // 단답형 
    else if(data.format == 3) {
        postDetailId = `${data.postDetailId}`;
        var pi = new Array();
        pi[0] = Number(postDetailId);
        semipostDetailResults[count-1][0] = pi;
        var SurveyQ = `<div class="question">
        <span class="Q1">${count + ".   " + data.question}</span>

        <input type="text" class="Qtype3" maxlength="30" id="${count * 10}"
        ></input>
        </div>`
        $SurveyQuestion.insertAdjacentHTML('beforeend',SurveyQ);
    }
    // 장문형
    //         <span class="required">필수</span> 빈 줄에 있던 거 주석
    else if(data.format == 4) {
        postDetailId = `${data.postDetailId}`;
        var pi = new Array();
        pi[0] = Number(postDetailId);
        semipostDetailResults[count-1][0] = pi;
        var SurveyQ = `<div class="question">
        <span class="Q1">${count + ".   " + data.question}</span>

        <textarea class="Qtype4" cols="108" rows="10" id="${count * 10}"
        ></textarea>
        </div>`
        $SurveyQuestion.insertAdjacentHTML('beforeend',SurveyQ);
    }
}


//나의 답변 확인
const fetchAnswer = () => {
    var requestOptions = {
        method: "GET",
        headers: {'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, }
    };

    fetch(
        `http://seolmunzip.shop:9000/users/answer/${rpostId}`,
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult);
            if(webResult.code == 2002) {
                fetchTokenCheck();
                fetchAnswer(); // 여기 다시
            }
            console.log(webResult.result);
            webResult.result.map(item => answerTemplate2(item));
        })
        .catch((error) => console.log("error", error));

}

function answerTemplate2(data) {
    cnt += 10;

    if(data.format == 1) {
        var questions = document.getElementById(`${parseInt(cnt) + parseInt(data.result)}`);
        console.log(parseInt(cnt) + parseInt(data.result));
        questions.checked = true
        console.log(questions);
        console.log(questions.checked);
    } else if (data.format == 2) {
        for(i=0;i<data.result.length;i++){
            console.log(data.result[i]);
            var questions = document.getElementById(`${parseInt(cnt) + parseInt(data.result[i])}`);
            questions.checked = true
            console.log(questions);
            console.log(questions.checked);
        } 
    } else if (data.format == 3) {
        var questions = document.getElementById(`${parseInt(cnt)}`);
        questions.value = data.result;
        console.log(questions);
        console.log(questions.value);
    } else if (data.format == 4) {
        var questions = document.getElementById(`${parseInt(cnt)}`);
        questions.value = data.result;
        console.log(questions);
        console.log(questions.value);
    }
}

function gotoDetail() {
    history.back();
}
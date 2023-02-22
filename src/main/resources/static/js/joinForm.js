const $SurveyQuestion = document.querySelector("#SurveyQuestion");
var questions = document.getElementById("SurveyQuestion");
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
var semipostDetailResults = new Array();
var postDetailId;
var formats = new Array(); // 여기에 format 넣음. 
var checkboxes = new Array();
var postLength = 0;

function gotoParticipatedSurvey() {
    var link="../participatedSurvey.html";
    location.href=link;
}

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
                postLength = webResult.result.length;
                console.log(postLength);
                webResult.result.map(item => SurveyInTemplate(item));
            }
            if(webResult.code == 2002) {
                fetchTokenCheck();
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
                        console.log(webResult.code);
                        if(webResult.code == 1000) {
                            postLength = webResult.result.length;
                            console.log(postLength);
                            webResult.result.map(item => SurveyInTemplate(item));
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
            inputDiv.value = j+1;

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
            inputDiv.value = j+1;

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

        <input type="text" class="Qtype3" maxlength="30"
        placeholder="자유롭게 적어주세요."
        onfocus="this.placeholder = ''" 
        onblur="this.placeholder = '자유롭게 적어주세요.'"></textarea>
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

        <textarea class="Qtype4" cols="108" rows="10" 
        placeholder=" 자유롭게 적어주세요."
        onfocus="this.placeholder = ''" 
        onblur="this.placeholder = ' 자유롭게 적어주세요.'"></textarea>
        </div>`
        $SurveyQuestion.insertAdjacentHTML('beforeend',SurveyQ);
    }
}

// 답변 등록
function sendPost() {
    for(var i=0; i<postLength; i++) {
        var tt = semipostDetailResults[i][0];
        if( formats[i] == 1 ) {
            var pi = new Array();
            pi[0] = $(`input[name=${tt}]:checked`).val();

            semipostDetailResults[i][1] = pi;
        }
        else if( formats[i] == 2 ) {
            const query = `input[name="${tt}"]:checked`;
            const selectedElements = document.querySelectorAll(query);
            
            // 선택된 목록의 개수 세기
            const selectedElementsCnt =selectedElements.length;

            checkboxes[i] = new Array(selectedElementsCnt);
            var j=0;
            selectedElements.forEach((el) => {
                checkboxes[i][j++] = el.value;
            });
            semipostDetailResults[i][1] = checkboxes[i];
            console.log(semipostDetailResults[i][1]);
        }
        else if( formats[i] == 3 ) {
            var pi = new Array();
            pi[0] = $(`input[type="text"]`).val();

            semipostDetailResults[i][1] = pi;
        }
        else if( formats[i] == 4 ) {
            var pi = new Array();
            pi[0] =  $(`textarea`).val();

            semipostDetailResults[i][1] = pi;
        }
    }
    console.log(semipostDetailResults);

    const sendList = {
        "postId" : rpostId,
        "postDetailResults" : semipostDetailResults
    }

    console.log(JSON.stringify(sendList));

    fetch(`http://seolmunzip.shop:9000/results` , {
        method: "POST",
        headers: {
            'X-ACCESS-TOKEN' : my_jwt,
            'Content-Type': 'application/json'            
        },
        body: JSON.stringify(sendList)
    })

    .then((response) => response.json())
    .then((response2) => {
        console.log(response2.message);
        gotoParticipatedSurvey();
    })
    .catch((error) => console.log("error", error))
}
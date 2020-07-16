

function alertModal(message, pageMoveUrl) {

    $.alert({
        title : "",
        content : message,
        onClose : function() {
            //if(pageMoveUrl != undefined){
            if(typeof pageMoveUrl=="undefined" || pageMoveUrl == ""){
                return false;
            }
            else
            {
                page_move(pageMoveUrl);

            }
        }
    });
}

function alerttitleModal(title, message, pageMoveUrl) {
    $.alert({
        title : title,
        content : message,
        onClose : function () {
            if(typeof pageMoveUrl=="undefined" || pageMoveUrl == ""){
                return false;
            }
            else
            {
                page_move(pageMoveUrl);

            }
        }
    });

}

function chkName(str){
    var regExp = /[0-9]|[~!@#$%^&*()_+|<>?\\\/\\\'\";:{}]/;

    if(regExp.test(str))
        alertModal("이름 형식이 잘못 되었습니다.");
    else{
        return true;
    }
}

function chkPhone(str){

    var regExp =/01[016789]{1}[0-9]{3,4}[0-9]{4}/;

    if(regExp.test(str)) return true;
    else {
        alertModal("전화번호 형식이 잘 못 되었습니다. 확인 바랍니다.");
    }
}


function chkEmail(str) {

    //var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;


    if (regExp.test(str)) return true;
    else {
        alertModal("사용자 아이디의 이메일 형식이 잘 못 되었습니다. 확인 바랍니다.");
        return false;
    }
}

function chkEmailform(str) {

    //var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;


    if (regExp.test(str)) return true;
    else {
        alertModal("사용자 이메일 형식이 잘 못 되었습니다. 확인 바랍니다.");
        return false;
    }
}


function chkPassword(str) {

    //var regExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
    var regExp = /^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;

    if (regExp.test(str)) return true;
    else {
        //alert('비밀번호는 8자 이상이어야 하며, 숫자/대문자/소문자/특수문자를 모두 포함해야 합니다.');
        alertModal('비밀번호는 8자 이상이어야 하며, 숫자/영문/특수문자를 모두 포함해야 합니다.');
        return false;
    }
}

function commonMessageModal(title, msg) {

    console.log("Modal alert invoked!")

    var $title = title;
    var $msg = msg;

    $("body").append("<div id='newModal'>");
    $("#newModal").addClass("modal fade");
    $("#newModal").attr("role","dialog");
    $("#newModal").attr("aria-hidden","true");
    $("#newModal").css("display","none");

    var addHiddenBtn = "<button id=\"hidden_btn\" type=\"button\" data-toggle=\"modal\" style='visibility: hidden;display: none'></button>"

    var addHtml = " <div class='modal-dialog modal-center' role='document'>\n" +
        "  <div class='modal-content'>\n" +
        "   <div class='modal-header text-center'>\n" +
        "    <h4 class='modal-title'>"+$title+"</h4>\n" +
        "    <button type='button' class='close' data-dismiss='modal' aria-label='Close'>\n" +
        "     <span aria-hidden='true'>&times;</span>\n" +
        "    </button>\n" +
        "   </div>\n" +
        "   <form id='delete_book_list'>\n" +
        "    <div class='modal-body'>\n" +
        "     <h6>"+$msg+"</h6>\n" +
        "    </div>\n" +
        "    <div class='modal-footer'>\n" +
        "     <button class='btn btn-default' data-dismiss='modal' id='close_btn'>확인</button>\n" +
        "    </div>\n" +
        "   </form>\n" +
        "  </div>\n" +
        " </div>";

    $("#newModal").html(addHtml);

    $("body").append(addHiddenBtn);

    $("#hidden_btn").attr("data-target","#newModal");

    $("#hidden_btn").trigger('click');

    $("#hidden_btn").remove();

}

function ajaxsubmit(data, url, returnUrl) {
    $.ajax({
        type:"POST",
        data: data,
        url: url,
        dataType: "json",
        processData: false,
        contentType: false,
        success: function (data) {
            page_move(returnUrl);
        },
        error: function(req,status,error) {

        }
    });
}

function JSONToCSVConvertor(JSONData, fileName, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;

    var CSV = '';

    //라벨 헤더 생성
    if (ShowLabel) {
        var row = "";
        //배열의 첫번째 인덱스에서 라밸 추출
        for (var index in arrData[0]) {
            //쉽표로 구분된 값으로 변환
            row += index + ',';
        }
        //마지막 쉼표 제거
        row = row.slice(0, -1);

        //row에 줄바꿈 추가
        CSV += row + '\r\n';
    }

    //각 행 추출
    for (var i = 0; i < arrData.length; i++) {
        var row = "";

        //각 열 추출
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        //인덱스 0부터 시작하기때문에 전체 개수 -1만큼 slice
        row.slice(0, row.length - 1);

        //줄바꿈 추가
        CSV += row + '\r\n';
    }

    if (CSV == '') {
        alert("Invalid data");
        return;
    }

    //파일 형식 초기화 (\uFEFF로 와 encodeURI로 한글 깨짐 해결)
    var uri = 'text/csv;charset=utf-8,\uFEFF' + encodeURI(CSV);

    //임시 a 태그 생성
    var link = document.createElement("a");
    //link.href = uri;
    link.href = uri;

    //레이아웃에 보이지 않게 visibility:hidden
    link.style = "visibility:hidden";

    link.download = fileName + ".csv";

    //생성한 링크 삭제
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);

}

//json파일을 테이블로 변환 해 엑셀파일로(.xls) 다운로드 하기 위한 함수
function JsonToTableToXLSConvertor(jData, filename) {

    //브라우저 에이전트를 소문자 형태로 추출
    var agent = navigator.userAgent.toLowerCase();

    //xls의 마메 타입 변수에 저장
    var data_type = 'data:application/vnd.ms-excel;charset=utf-8,';

    //테이블의 CSS와 테이블 구조를 저장할 변수 초기화
    var css_html = "";
    var table_html = "";

    //매개변수로 받은 데이터가 오브젝트가 아니라면 parse를 통해 객체로 변환
    var arrJSON = typeof jData != 'object' ? JSON.parse(jData) : jData;
    var $table = $('<table id=newTable/>');
    var $headerTr = $('<tr/>');

    //테이블 구조 생성
    for (var index in arrJSON[0]) {
        $headerTr.append($('<th/>').html(index));
    }
    $table.append($headerTr);
    for (var i = 0; i < arrJSON.length; i++) {
        var $tableTr = $('<tr/>');
        for (var index in arrJSON[i]) {
            $tableTr.append($('<td/>').html(arrJSON[i][index]));
        }
        $table.append($tableTr);
    }

    //테이블이 저장된 변수를 body에 생성
    $('body').append($table);

    //테이블 투명화
    $('#newTable').css('visibility','hidden');

    //에이전트에서 Trident 문자열이 존재 할 시 MSIE로 구분
    if (navigator.userAgent.search('Trident') != -1) {
        console.log("msie browser invoked!")

        //UTF의 바이트를 읽는 방향 지정
        var BOM = "\uFEFF";

        //테이블을 쉼표구분 문자열로 변경
        var table = document.getElementById("newTable");
        var csvString = BOM;

        for (var rowCnt = 0; rowCnt < table.rows.length; rowCnt++) {
            var rowData = table.rows[rowCnt].cells;
            for (var colCnt = 0; colCnt < rowData.length; colCnt++) {
                var columnData = rowData[colCnt].innerHTML;

                if (columnData == null || columnData.length == 0) {
                    columnData = "".replace(/"/g, '""');
                }
                else {
                    columnData = columnData.toString().replace(/"/g, '""');
                    columnData = columnData.toString().replace(/&gt;/g, '>');
                }
                csvString = csvString + '"' + columnData + '",';

            }
            csvString = csvString.substring(0, csvString.length - 1);
            csvString = csvString.replace("&gt;", "\>");
            csvString = csvString + "\r\n";

        }
        csvString = csvString.substring(0, csvString.length - 1);

        //문자열을 decodeURIComponent로 디코딩 후 생성된 Blob에 저장
        var blob = new Blob([decodeURIComponent(csvString)], {
            type: data_type
        });

        //msSaveOrOpenBlob을 통해 blob의 내용을 오픈
        window.navigator.msSaveOrOpenBlob(blob, filename);

        $('#newTable').remove();

    } else {

        console.log("not MSIE invoked!")

        //a 태그 생성 후 테이블 구조를 encodeURIComponent를 통해 인코딩 후 다운로드
        var a = document.createElement('a');

        table_html = $('#newTable')[0].outerHTML;

        table_html = table_html.replace(/<tfoot[\s\S.]*tfoot>/gmi, '');

        css_html = '<style>td {border: 0.5pt solid #c0c0c0} .tRight { text-align:right} .tLeft { text-align:left} </style>';

        a.href = data_type + ',' + encodeURIComponent('<html><head>' + css_html + '</' + 'head><body>' + table_html + '</body></html>');

        a.download = filename;

        a.click();

        $('#newTable').remove();

    } //else

} //end function

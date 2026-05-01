// 社員画面で共通利用するフロント入力チェック。
// 目的: サーバー送信前に基本的な入力ミスを早期検知し、最初の1件をアラート表示する。
var EmpVal = {
    // id で要素を取得し、未存在時は空文字を返す（null参照回避）
    val: function (id) {
        var el = document.getElementById(id);
        return el ? el.value : '';
    },
    // name 属性で要素を取得し、未存在時は空文字を返す
    selVal: function (name) {
        var el = document.querySelector('[name="' + name + '"]');
        return el ? el.value : '';
    },
    // 必須チェック（空白のみも未入力として扱う）
    req: function (errors, v, label) {
        if (!v || !v.trim()) { errors.push(label + 'を入力してください。'); return false; }
        return true;
    },
    alphanum: function (errors, v, label) {
        if (v && !/^[A-Za-z0-9]+$/.test(v)) errors.push(label + 'は半角英数字で入力してください。');
    },
    alpha: function (errors, v, label) {
        if (v && !/^[A-Za-z]+$/.test(v)) errors.push(label + 'は英字で入力してください。');
    },
    fullWidth: function (errors, v, label) {
        if (v && /[\x00-\x7F]/.test(v)) errors.push(label + 'は全角で入力してください。');
    },
    numeric: function (errors, v, label) {
        if (v && !/^\d+$/.test(v)) errors.push(label + 'は数字のみで入力してください。');
    },
    maxLen: function (errors, v, max, label) {
        if (v && v.length > max) errors.push(label + 'は' + max + '文字以下で入力してください。');
    },
    // 退社日の形式と日付範囲を検証し、必要なら補正してユーザーへ通知する
    taisyaDate: function (errors) {
        var taisya = EmpVal.val('taisyaDate');
        if (!taisya) return;
        if (!/^\d{4}-\d{2}-\d{2}$/.test(taisya)) {
            errors.push('退社日はYYYY-MM-DD形式で入力してください。');
            return;
        }
        var p = taisya.split('-');
        var y = +p[0], m = +p[1], d = +p[2];
        if (m < 1) m = 1; if (m > 12) m = 12;
        var last = new Date(y, m, 0).getDate();
        if (d < 1) d = 1; if (d > last) d = last;
        var fixed = y + '-' + String(m).padStart(2, '0') + '-' + String(d).padStart(2, '0');
        if (fixed !== taisya) {
            document.getElementById('taisyaDate').value = fixed;
            errors.push('退社日の値が範囲外のため ' + fixed + ' に修正しました。確認後、再度送信してください。');
        }
    }
};

// 検索フォームの在籍チェック（社員一覧画面）
function validateSearch() {
    var working = document.getElementById('active').checked;
    var notWorking = document.getElementById('inactive').checked;
    if (!working && !notWorking) {
        alert('在籍と非在籍がいずれにしても、\n１つのチェックが必須です。');
        return false;
    }
    return true;
}

// OS技術スキルのクリックトグル（登録画面）
// canEditTechSkills は th:inline で直前に定義される
if (typeof canEditTechSkills !== 'undefined' && canEditTechSkills) {
    // 先頭の空文字はクリック4回目で「未選択」に戻るリセット状態を表す
    var LEVELS = ['', '◎', '○', '△'];
    document.querySelectorAll('.tech-click-cell').forEach(function (td) {
        td.addEventListener('click', function () {
            var badge = td.querySelector('.tech-badge');
            var hidden = td.querySelector('input[type=hidden]');
            var id = td.getAttribute('data-id');
            var current = LEVELS.indexOf(badge.textContent.trim());
            if (current < 0) current = 0;
            var next = (current + 1) % LEVELS.length;
            badge.textContent = LEVELS[next];
            if (next === 0) {
                hidden.removeAttribute('name'); hidden.value = ''; td.style.background = '';
            } else {
                hidden.setAttribute('name', td.getAttribute('data-group'));
                hidden.value = id + '-' + next; td.style.background = '#eef4ff';
            }
        });
    });
}

// 登録・更新フォームの送信バリデーション
// 処理方針: エラー配列に蓄積し、1件以上あれば送信を止めて先頭メッセージを表示する
(function () {
    var form = document.getElementById('employeeForm');
    if (!form) return;

    // employeecode が text 入力なら登録画面、hidden なら更新画面
    var codeEl = document.getElementById('employeecode');
    var isRegister = codeEl && codeEl.type !== 'hidden';

    form.addEventListener('submit', function (e) {
        var errors = [];

        if (isRegister) {
            var code = EmpVal.val('employeecode');
            if (EmpVal.req(errors, code, '社員コード')) {
                EmpVal.alphanum(errors, code, '社員コード');
                EmpVal.maxLen(errors, code, 10, '社員コード');
            }
        }

        var lKanji = EmpVal.val('lastNameKanji'), fKanji = EmpVal.val('firstNameKanji');
        if (EmpVal.req(errors, lKanji, '社員名（漢字）姓')) { EmpVal.fullWidth(errors, lKanji, '社員名（漢字）姓'); EmpVal.maxLen(errors, lKanji, 15, '社員名（漢字）姓'); }
        if (EmpVal.req(errors, fKanji, '社員名（漢字）名')) { EmpVal.fullWidth(errors, fKanji, '社員名（漢字）名'); EmpVal.maxLen(errors, fKanji, 15, '社員名（漢字）名'); }

        var lKana = EmpVal.val('lastNameKana'), fKana = EmpVal.val('firstNameKana');
        if (EmpVal.req(errors, lKana, '社員名（カタカナ）セイ')) { EmpVal.fullWidth(errors, lKana, '社員名（カタカナ）セイ'); EmpVal.maxLen(errors, lKana, 15, '社員名（カタカナ）セイ'); }
        if (EmpVal.req(errors, fKana, '社員名（カタカナ）メイ')) { EmpVal.fullWidth(errors, fKana, '社員名（カタカナ）メイ'); EmpVal.maxLen(errors, fKana, 15, '社員名（カタカナ）メイ'); }

        var fEigo = EmpVal.val('firstNameEigo'), lEigo = EmpVal.val('lastNameEigo');
        if (EmpVal.req(errors, fEigo, '社員名（英語）first name')) { EmpVal.alpha(errors, fEigo, '社員名（英語）first name'); EmpVal.maxLen(errors, fEigo, 30, '社員名（英語）first name'); }
        if (EmpVal.req(errors, lEigo, '社員名（英語）last name')) { EmpVal.alpha(errors, lEigo, '社員名（英語）last name'); EmpVal.maxLen(errors, lEigo, 30, '社員名（英語）last name'); }

        EmpVal.req(errors, EmpVal.selVal('syozokuKaisya'), '所属会社');
        EmpVal.req(errors, EmpVal.val('nyuusyaDate'), '入社日');
        EmpVal.taisyaDate(errors);
        EmpVal.req(errors, EmpVal.selVal('syokugyoKind'), '職業種類');

        if (isRegister) {
            var bkCode = EmpVal.val('kinyukikan_code'), bkName = EmpVal.val('kinyukikan_name');
            var brCode = EmpVal.val('siten_code'), brName = EmpVal.val('siten_name');
            var kNum = EmpVal.val('kouzaNum'), meigi = EmpVal.val('meigiName');
            EmpVal.numeric(errors, bkCode, '金融機関コード'); EmpVal.maxLen(errors, bkCode, 10, '金融機関コード');
            EmpVal.fullWidth(errors, bkName, '金融機関名'); EmpVal.maxLen(errors, bkName, 50, '金融機関名');
            EmpVal.numeric(errors, brCode, '支店名コード'); EmpVal.maxLen(errors, brCode, 10, '支店名コード');
            EmpVal.fullWidth(errors, brName, '支店名'); EmpVal.maxLen(errors, brName, 50, '支店名');
            EmpVal.numeric(errors, kNum, '口座番号'); EmpVal.maxLen(errors, kNum, 10, '口座番号');
            EmpVal.maxLen(errors, meigi, 50, '名義人');
        }

        if (errors.length > 0) { alert(errors[0]); e.preventDefault(); }
    });
})();

// 作業完了画面のカウントダウン
(function () {
    var el = document.getElementById('countdown');
    if (!el) return;
    var sec = 3;
    el.textContent = sec + '秒後に一覧画面へ戻ります。';
    var t = setInterval(function () {
        sec--;
        if (sec <= 0) {
            clearInterval(t);
            location.href = '/employee/list';
        } else {
            el.textContent = sec + '秒後に一覧画面へ戻ります。';
        }
    }, 1000);
})();

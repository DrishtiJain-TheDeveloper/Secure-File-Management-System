let currentUser = null;
let currentRole = null;
let writeAppend = false;
let currentView = 'grid';
let allFiles = [];
let activityLog = [];

// ── PARTICLES ─────────────────────────────────────────────────────────────────
(function initParticles() {
    const canvas = document.getElementById('particles-canvas');
    const ctx = canvas.getContext('2d');
    let particles = [];
    function resize() { canvas.width = window.innerWidth; canvas.height = window.innerHeight; }
    resize();
    window.addEventListener('resize', resize);
    for (let i = 0; i < 60; i++) particles.push({
        x: Math.random() * canvas.width, y: Math.random() * canvas.height,
        r: Math.random() * 1.5 + 0.3, vx: (Math.random() - 0.5) * 0.3, vy: (Math.random() - 0.5) * 0.3,
        a: Math.random() * 0.5 + 0.1
    });
    function draw() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        particles.forEach(p => {
            ctx.beginPath(); ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
            ctx.fillStyle = `rgba(99,102,241,${p.a})`; ctx.fill();
            p.x += p.vx; p.y += p.vy;
            if (p.x < 0 || p.x > canvas.width) p.vx *= -1;
            if (p.y < 0 || p.y > canvas.height) p.vy *= -1;
        });
        // draw lines between close particles
        for (let i = 0; i < particles.length; i++)
            for (let j = i + 1; j < particles.length; j++) {
                const dx = particles[i].x - particles[j].x, dy = particles[i].y - particles[j].y;
                const dist = Math.sqrt(dx*dx + dy*dy);
                if (dist < 100) {
                    ctx.beginPath(); ctx.moveTo(particles[i].x, particles[i].y);
                    ctx.lineTo(particles[j].x, particles[j].y);
                    ctx.strokeStyle = `rgba(99,102,241,${0.08 * (1 - dist/100)})`; ctx.stroke();
                }
            }
        requestAnimationFrame(draw);
    }
    draw();
})();

// ── KEYBOARD SHORTCUTS ────────────────────────────────────────────────────────
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') document.querySelectorAll('.modal-overlay.open').forEach(m => m.classList.remove('open'));
});

// ── TAB SWITCHING ─────────────────────────────────────────────────────────────
function switchTab(tab) {
    document.querySelectorAll('.tab').forEach((t, i) =>
        t.classList.toggle('active', (tab === 'login' && i === 0) || (tab === 'register' && i === 1)));
    document.getElementById('login-form').style.display = tab === 'login' ? 'block' : 'none';
    document.getElementById('register-form').style.display = tab === 'register' ? 'block' : 'none';
    clearMsg('login-msg'); clearMsg('register-msg');
    if (tab === 'register') loadSecurityQuestions();
}

function backToStep1() {
    document.getElementById('login-step-1').style.display = 'block';
    document.getElementById('login-step-2').style.display = 'none';
    clearMsg('login-msg');
}

function togglePwd(id, btn) {
    const input = document.getElementById(id);
    input.type = input.type === 'password' ? 'text' : 'password';
    btn.style.opacity = input.type === 'text' ? '1' : '0.5';
}

function checkPasswordStrength(val) {
    const fill = document.getElementById('strength-fill');
    const label = document.getElementById('strength-label');
    if (!fill) return;
    let score = 0;
    if (val.length >= 8) score++;
    if (/[A-Z]/.test(val)) score++;
    if (/[0-9]/.test(val)) score++;
    if (/[^A-Za-z0-9]/.test(val)) score++;
    const levels = [
        { w: '0%', c: 'transparent', t: '' },
        { w: '25%', c: '#ef4444', t: 'Weak' },
        { w: '50%', c: '#f59e0b', t: 'Fair' },
        { w: '75%', c: '#38bdf8', t: 'Good' },
        { w: '100%', c: '#22c55e', t: 'Strong' }
    ];
    const l = levels[score];
    fill.style.width = l.w; fill.style.background = l.c;
    label.textContent = l.t; label.style.color = l.c;
}

// ── AUTH ──────────────────────────────────────────────────────────────────────
async function loadSecurityQuestions() {
    const res = await fetch('/api/auth/questions');
    const data = await res.json();
    document.getElementById('reg-question').innerHTML =
        data.questions.map(q => `<option value="${q}">${q}</option>`).join('');
}

async function fetchSecurityQuestion() {
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value;
    if (!username || !password) return showMsg('login-msg', 'Enter username and password.', 'error');
    setLoading('btn-next', true);
    const res = await fetch(`/api/auth/security-question?username=${encodeURIComponent(username)}`);
    const data = await res.json();
    setLoading('btn-next', false);
    if (data.success) {
        document.getElementById('security-question-label').textContent = data.question;
        document.getElementById('login-step-1').style.display = 'none';
        document.getElementById('login-step-2').style.display = 'block';
        document.getElementById('login-security-answer').focus();
        clearMsg('login-msg');
    } else showMsg('login-msg', data.message, 'error');
}

async function login() {
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value;
    const securityAnswer = document.getElementById('login-security-answer').value.trim();
    if (!securityAnswer) return showMsg('login-msg', 'Enter your security answer.', 'error');
    setLoading('btn-login', true);
    const res = await fetch('/api/auth/login', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, securityAnswer })
    });
    const data = await res.json();
    setLoading('btn-login', false);
    if (data.success) { currentUser = data.username; currentRole = data.role; showDashboard(); }
    else showMsg('login-msg', data.message, 'error');
}

async function register() {
    const username = document.getElementById('reg-username').value.trim();
    const password = document.getElementById('reg-password').value;
    const securityQuestion = document.getElementById('reg-question').value;
    const securityAnswer = document.getElementById('reg-answer').value.trim();
    if (!username || !password || !securityAnswer)
        return showMsg('register-msg', 'All fields are required.', 'error');
    setLoading('btn-register', true);
    const res = await fetch('/api/auth/register', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password, securityQuestion, securityAnswer })
    });
    const data = await res.json();
    setLoading('btn-register', false);
    showMsg('register-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) setTimeout(() => { switchTab('login'); document.getElementById('login-username').value = username; }, 1200);
}

async function logout() {
    await fetch('/api/auth/logout', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser })
    });
    currentUser = null; currentRole = null; allFiles = []; activityLog = [];
    document.getElementById('dashboard-section').style.display = 'none';
    document.getElementById('auth-section').style.display = 'flex';
    backToStep1();
    ['login-username','login-password','login-security-answer'].forEach(id => document.getElementById(id).value = '');
}

// ── DASHBOARD ─────────────────────────────────────────────────────────────────
function showDashboard() {
    document.getElementById('auth-section').style.display = 'none';
    document.getElementById('dashboard-section').style.display = 'block';
    document.getElementById('nav-username').textContent = currentUser;
    document.getElementById('nav-avatar').textContent = currentUser[0].toUpperCase();
    const badge = document.getElementById('nav-role');
    badge.textContent = currentRole;
    badge.className = 'role-badge' + (currentRole === 'admin' ? ' admin' : '');
    buildSidebar();
    showPanel('panel-files');
    loadMyFiles();
}

function buildSidebar() {
    const items = [
        { icon: '📁', label: 'File Operations', panel: 'panel-files' },
        { icon: '🔗', label: 'Share Center', panel: 'panel-share' }
    ];
    if (currentRole === 'admin') items.push({ icon: '🛡️', label: 'Admin Panel', panel: 'panel-admin' });
    document.getElementById('sidebar-menu').innerHTML = items.map(i =>
        `<li><a onclick="showPanel('${i.panel}')" id="nav-${i.panel}">${i.icon} ${i.label}</a></li>`
    ).join('');
    renderActivity();
}

function showPanel(id) {
    document.querySelectorAll('.panel').forEach(p => p.style.display = 'none');
    document.getElementById(id).style.display = 'block';
    document.querySelectorAll('.sidebar ul li a').forEach(a => a.classList.remove('active'));
    const link = document.getElementById('nav-' + id);
    if (link) link.classList.add('active');
    const searchWrap = document.getElementById('nav-search-wrap');
    if (searchWrap) searchWrap.style.display = id === 'panel-files' ? 'flex' : 'none';
    if (id === 'panel-admin') loadUsers();
    if (id === 'panel-share') initShareCenter();
}

// ── STATS ─────────────────────────────────────────────────────────────────────
function renderFileStats(files) {
    const total = files.length;
    const types = {};
    files.forEach(f => {
        const ext = f.split('.').pop().toLowerCase();
        types[ext] = (types[ext] || 0) + 1;
    });
    const topType = Object.entries(types).sort((a,b) => b[1]-a[1])[0];
    document.getElementById('stats-row').innerHTML = `
        <div class="stat-card">
            <div class="stat-icon">📁</div>
            <div class="stat-value" data-target="${total}">0</div>
            <div class="stat-label">Total Files</div>
        </div>
        <div class="stat-card green">
            <div class="stat-icon">🔒</div>
            <div class="stat-value" data-target="${total}">0</div>
            <div class="stat-label">Encrypted</div>
        </div>
        <div class="stat-card yellow">
            <div class="stat-icon">📝</div>
            <div class="stat-value">${topType ? topType[0].toUpperCase() : '—'}</div>
            <div class="stat-label">Top File Type</div>
        </div>`;
    animateCounters();
    document.getElementById('file-count-badge').textContent = total;
}

function animateCounters() {
    document.querySelectorAll('.stat-value[data-target]').forEach(el => {
        const target = parseInt(el.dataset.target);
        let current = 0;
        const step = Math.max(1, Math.ceil(target / 20));
        const timer = setInterval(() => {
            current = Math.min(current + step, target);
            el.textContent = current;
            if (current >= target) clearInterval(timer);
        }, 40);
    });
}

// ── ACTIVITY LOG ──────────────────────────────────────────────────────────────
function logActivity(text, type = 'default') {
    const now = new Date();
    const time = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    activityLog.unshift({ text, type, time });
    if (activityLog.length > 8) activityLog.pop();
    renderActivity();
}

function renderActivity() {
    const list = document.getElementById('activity-list');
    if (!list) return;
    if (activityLog.length === 0) {
        list.innerHTML = '<li style="padding:8px 12px;font-size:11px;color:var(--text-dim)">No recent activity</li>';
        return;
    }
    list.innerHTML = activityLog.map(a => `
        <li class="activity-item">
            <div class="activity-dot ${a.type}"></div>
            <div>
                <div class="activity-text">${a.text}</div>
                <div class="activity-time">${a.time}</div>
            </div>
        </li>`).join('');
}

// ── FILE LIST ─────────────────────────────────────────────────────────────────
async function loadMyFiles() {
    const container = document.getElementById('my-files-list');
    container.innerHTML = `<div class="empty-state"><div class="empty-icon">⏳</div>Loading files...</div>`;
    const res = await fetch(`/api/files/list?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    allFiles = data.success ? data.files : [];
    renderFiles(allFiles);
    renderFileStats(allFiles);
}

function renderFiles(files) {
    const container = document.getElementById('my-files-list');
    container.className = currentView === 'grid' ? 'files-grid' : 'files-list-view';
    if (files.length === 0) {
        container.innerHTML = `<div class="empty-state">
            <div class="empty-icon">📭</div>
            <p>No files yet.</p>
            <button class="btn btn-primary btn-sm" onclick="openModal('modal-create')">Create your first file</button>
        </div>`;
        return;
    }
    container.innerHTML = files.map(f => {
        const name = cleanFileName(f);
        const icon = getFileIcon(name);
        return `<div class="file-card ${currentView === 'list' ? 'list-item' : ''}">
            <div class="file-card-icon">${icon}</div>
            <div class="file-card-info">
                <div class="file-card-name" title="${name}">${name}</div>
                <div class="file-card-meta">${getFileExt(name).toUpperCase() || 'FILE'}</div>
            </div>
            <div class="file-card-actions">
                <button class="btn btn-xs btn-outline" title="Read" onclick="quickRead('${name}')">📖</button>
                <button class="btn btn-xs btn-outline" title="Share" onclick="quickShare('${name}')">🔗</button>
                <button class="btn btn-xs btn-danger" title="Delete" onclick="quickDelete('${name}')">🗑</button>
            </div>
        </div>`;
    }).join('');
}

function cleanFileName(f) {
    return f.split('/').pop().split('\\').pop().replace(currentUser + '_', '').replace('shared_files/', '');
}

function getFileIcon(name) {
    const ext = getFileExt(name);
    return { txt:'📝', csv:'📊', json:'📋', pdf:'📕', png:'🖼️', jpg:'🖼️', jpeg:'🖼️', xml:'📰', md:'📄' }[ext] || '📄';
}

function getFileExt(name) { return name.includes('.') ? name.split('.').pop().toLowerCase() : ''; }

function setView(v) {
    currentView = v;
    document.getElementById('view-grid').classList.toggle('active', v === 'grid');
    document.getElementById('view-list').classList.toggle('active', v === 'list');
    renderFiles(allFiles);
}

function filterFiles(query) {
    const filtered = allFiles.filter(f => cleanFileName(f).toLowerCase().includes(query.toLowerCase()));
    renderFiles(filtered);
    document.getElementById('file-count-badge').textContent = filtered.length;
}

// ── QUICK ACTIONS FROM FILE CARD ──────────────────────────────────────────────
function quickRead(name) {
    openModal('modal-read');
    document.getElementById('read-filename').value = name;
}
let currentModalShareFile = '';

function quickShare(name) {
    currentModalShareFile = name;
    openModal('modal-share');
    // show file preview in modal
    const preview = document.getElementById('modal-share-file-preview');
    if (preview) {
        preview.innerHTML = `<span class="smfp-icon">${getFileIcon(name)}</span><span>${name}</span>`;
        preview.style.display = 'flex';
    }
    // reset recipient
    document.getElementById('modal-selected-recipient').value = '';
    document.getElementById('modal-share-recipient').value = '';
    const chip = document.getElementById('modal-recipient-chip');
    if (chip) { chip.style.display = 'none'; chip.innerHTML = ''; }
    loadModalRecipients();
}
function quickDelete(name) {
    openModal('modal-delete-file');
    document.getElementById('delete-filename').value = name;
}

// ── FILE OPERATIONS ───────────────────────────────────────────────────────────
async function createFile() {
    const fileName = document.getElementById('create-filename').value.trim();
    if (!fileName) return showMsg('create-msg', 'Enter a file name.', 'error');
    setLoading('btn-create', true);
    const res = await fetch('/api/files/create', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName })
    });
    const data = await res.json();
    setLoading('btn-create', false);
    showMsg('create-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) {
        loadMyFiles();
        logActivity(`Created "${fileName}"`, 'success');
        toast('✅ File created', 'success');
        setTimeout(() => closeModal('modal-create'), 1200);
    }
}

async function writeFile() {
    const fileName = document.getElementById('write-filename').value.trim();
    const content = document.getElementById('write-content').value;
    const shift = document.getElementById('write-shift').value;
    if (!fileName) return showMsg('write-msg', 'Enter a file name.', 'error');
    if (!content) return showMsg('write-msg', 'Content cannot be empty.', 'error');
    setLoading('btn-write', true);
    const res = await fetch('/api/files/write', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName, content, shift, append: writeAppend })
    });
    const data = await res.json();
    setLoading('btn-write', false);
    showMsg('write-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) {
        logActivity(`Wrote to "${fileName}" (shift ${shift})`, 'success');
        toast('🔒 Content encrypted & written', 'success');
        setTimeout(() => closeModal('modal-write'), 1200);
    }
}

async function readFile() {
    const fileName = document.getElementById('read-filename').value.trim();
    const shift = document.getElementById('read-shift').value;
    if (!fileName) return showMsg('read-msg', 'Enter a file name.', 'error');
    setLoading('btn-read', true);
    const res = await fetch('/api/files/read', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName, shift })
    });
    const data = await res.json();
    setLoading('btn-read', false);
    const resultEl = document.getElementById('read-result');
    if (data.success) {
        clearMsg('read-msg');
        document.getElementById('read-content').textContent = data.content || '(empty file)';
        resultEl.style.display = 'block';
        logActivity(`Read "${fileName}"`, 'default');
    } else {
        showMsg('read-msg', data.message, 'error');
        resultEl.style.display = 'none';
    }
}

function copyContent() {
    const text = document.getElementById('read-content').textContent;
    navigator.clipboard.writeText(text).then(() => toast('📋 Copied to clipboard', 'success'));
}

function downloadContent() {
    const text = document.getElementById('read-content').textContent;
    const name = document.getElementById('read-filename').value.trim() || 'file.txt';
    const blob = new Blob([text], { type: 'text/plain' });
    const a = document.createElement('a'); a.href = URL.createObjectURL(blob);
    a.download = name; a.click();
    toast('⬇ Downloaded', 'success');
}

async function getMetadata() {
    const fileName = document.getElementById('meta-filename').value.trim();
    if (!fileName) return showMsg('meta-msg', 'Enter a file name.', 'error');
    setLoading('btn-meta', true);
    const res = await fetch('/api/files/metadata', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName })
    });
    const data = await res.json();
    setLoading('btn-meta', false);
    const resultEl = document.getElementById('meta-result');
    if (data.success) {
        clearMsg('meta-msg');
        resultEl.style.display = 'grid';
        resultEl.innerHTML = [
            { label: 'File Name', value: data.name },
            { label: 'Size', value: formatBytes(data.size) },
            { label: 'Last Modified', value: data.lastModified },
            { label: 'Full Path', value: data.path }
        ].map(m => `<div class="meta-card"><div class="meta-card-label">${m.label}</div><div class="meta-card-value">${m.value}</div></div>`).join('');
    } else {
        showMsg('meta-msg', data.message, 'error');
        resultEl.style.display = 'none';
    }
}

async function shareFile() {
    const fileName = document.getElementById('share-filename') ?
        document.getElementById('share-filename').value.trim() : currentModalShareFile;
    const recipient = document.getElementById('modal-selected-recipient').value.trim();
    if (!fileName) return showMsg('share-msg', 'No file selected.', 'error');
    if (!recipient) return showMsg('share-msg', 'Select a recipient.', 'error');
    setLoading('btn-share', true);
    const res = await fetch('/api/files/share', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName, recipient })
    });
    const data = await res.json();
    setLoading('btn-share', false);
    showMsg('share-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) {
        logActivity(`Shared "${fileName}" → ${recipient}`, 'success');
        toast(`🔗 Shared with ${recipient}`, 'success');
        updateInboxBadge();
        setTimeout(() => closeModal('modal-share'), 1200);
    }
}

async function deleteFile() {
    const fileName = document.getElementById('delete-filename').value.trim();
    if (!fileName) return showMsg('delete-file-msg', 'Enter a file name.', 'error');
    setLoading('btn-delete-file', true);
    const res = await fetch('/api/files/delete', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName })
    });
    const data = await res.json();
    setLoading('btn-delete-file', false);
    showMsg('delete-file-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) {
        loadMyFiles();
        logActivity(`Deleted "${fileName}"`, 'danger');
        toast('🗑 File deleted', 'success');
        setTimeout(() => closeModal('modal-delete-file'), 1200);
    }
}

// ── WRITE HELPERS ─────────────────────────────────────────────────────────────
function setWriteMode(append) {
    writeAppend = append;
    document.getElementById('mode-overwrite').classList.toggle('active', !append);
    document.getElementById('mode-append').classList.toggle('active', append);
}

function adjustShift(id, delta) {
    const input = document.getElementById(id);
    let val = Math.min(25, Math.max(1, parseInt(input.value || 1) + delta));
    input.value = val;
    if (id === 'write-shift') updateShiftPreview(val);
}

function updateShiftPreview(shift) {
    const preview = document.getElementById('shift-preview');
    if (!preview) return;
    const sample = 'hello';
    let enc = '';
    for (const c of sample) enc += String.fromCharCode(((c.charCodeAt(0) - 97 + shift) % 26) + 97);
    preview.textContent = `"${sample}" → "${enc}"`;
}

// ── ADMIN ─────────────────────────────────────────────────────────────────────
async function loadUsers() {
    const tbody = document.getElementById('users-tbody');
    tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:var(--text-muted);padding:20px">Loading...</td></tr>';
    const res = await fetch(`/api/admin/users?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    if (!data.success) { tbody.innerHTML = `<tr><td colspan="5">${data.message}</td></tr>`; return; }
    const users = data.users;
    document.getElementById('user-count-badge').textContent = users.length;
    document.getElementById('admin-stats-row').innerHTML = `
        <div class="stat-card">
            <div class="stat-icon">👥</div>
            <div class="stat-value" data-target="${users.length}">0</div>
            <div class="stat-label">Total Users</div>
        </div>
        <div class="stat-card green">
            <div class="stat-icon">🛡️</div>
            <div class="stat-value" data-target="${users.filter(u=>u.role==='admin').length}">0</div>
            <div class="stat-label">Admins</div>
        </div>
        <div class="stat-card yellow">
            <div class="stat-icon">📁</div>
            <div class="stat-value" data-target="${users.reduce((s,u)=>s+u.filesCount,0)}">0</div>
            <div class="stat-label">Total Files</div>
        </div>`;
    animateCounters();
    tbody.innerHTML = users.map(u => `
        <tr>
            <td><span class="user-row-avatar" style="background:${strColor(u.username)}">${u.username[0].toUpperCase()}</span>${u.username}</td>
            <td><span class="role-badge ${u.role==='admin'?'admin':''}">${u.role}</span></td>
            <td>${u.filesCount}</td>
            <td><span class="status-dot ${u.username === currentUser ? 'online' : ''}"></span>${u.username === currentUser ? 'Online' : 'Offline'}</td>
            <td>${u.username !== 'secureadmin'
                ? `<button class="btn btn-xs btn-danger" onclick="deleteUser('${u.username}')">Delete</button>`
                : '<span style="color:var(--text-dim)">Protected</span>'}</td>
        </tr>`).join('');
}

async function deleteUser(target) {
    if (!confirm(`Delete user "${target}"? This cannot be undone.`)) return;
    const res = await fetch(`/api/admin/users/${target}?username=${encodeURIComponent(currentUser)}`, { method: 'DELETE' });
    const data = await res.json();
    toast(data.success ? `🗑 User "${target}" deleted` : data.message, data.success ? 'success' : 'error');
    if (data.success) { logActivity(`Deleted user "${target}"`, 'danger'); loadUsers(); }
}

// ── MODALS ────────────────────────────────────────────────────────────────────
function openModal(id) {
    const modal = document.getElementById(id);
    modal.querySelectorAll('input:not([type=number]):not([type=hidden])').forEach(i => i.value = '');
    modal.querySelectorAll('textarea').forEach(t => t.value = '');
    modal.querySelectorAll('.msg').forEach(m => { m.textContent = ''; m.className = 'msg'; });
    const rr = document.getElementById('read-result'); if (rr) rr.style.display = 'none';
    const mr = document.getElementById('meta-result'); if (mr) mr.style.display = 'none';
    if (id === 'modal-write') { setWriteMode(false); updateShiftPreview(3); document.getElementById('write-char-count').textContent = '0 characters'; document.getElementById('write-word-count').textContent = '0 words'; }
    modal.classList.add('open');
    setTimeout(() => { const first = modal.querySelector('input:not([type=number]):not([type=hidden])'); if (first) first.focus(); }, 100);
}

function closeModal(id) { document.getElementById(id).classList.remove('open'); }
function overlayClose(e, id) { if (e.target.id === id) closeModal(id); }

// ── TOAST ─────────────────────────────────────────────────────────────────────
function toast(msg, type = 'success') {
    const stack = document.getElementById('toast-stack');
    const el = document.createElement('div');
    el.className = `toast-item ${type}`;
    el.innerHTML = `<span class="toast-icon">${type === 'success' ? '✅' : '❌'}</span><span>${msg}</span>`;
    stack.appendChild(el);
    requestAnimationFrame(() => el.classList.add('show'));
    setTimeout(() => { el.classList.remove('show'); setTimeout(() => el.remove(), 400); }, 3000);
}

// ── UTILS ─────────────────────────────────────────────────────────────────────
function showMsg(id, text, type) { const el = document.getElementById(id); if(el){el.textContent=text; el.className='msg '+type;} }
function clearMsg(id) { const el = document.getElementById(id); if(el){el.textContent=''; el.className='msg';} }

function setLoading(btnId, loading) {
    const btn = document.getElementById(btnId);
    if (!btn) return;
    if (loading) { btn.dataset.orig = btn.innerHTML; btn.innerHTML = '<span class="spinner"></span>'; btn.disabled = true; }
    else { btn.innerHTML = btn.dataset.orig || btn.innerHTML; btn.disabled = false; }
}

function formatBytes(b) {
    if (b === 0) return '0 B';
    const k = 1024, sizes = ['B','KB','MB','GB'];
    const i = Math.floor(Math.log(b) / Math.log(k));
    return parseFloat((b / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
}

function strColor(str) {
    let hash = 0;
    for (const c of str) hash = c.charCodeAt(0) + ((hash << 5) - hash);
    return `hsl(${hash % 360}, 55%, 40%)`;
}

// ── CHAR / WORD COUNTER ───────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
    const content = document.getElementById('write-content');
    if (content) {
        content.addEventListener('input', () => {
            const v = content.value;
            document.getElementById('write-char-count').textContent = v.length.toLocaleString() + ' characters';
            document.getElementById('write-word-count').textContent = (v.trim() ? v.trim().split(/\s+/).length : 0) + ' words';
            updateShiftPreview(parseInt(document.getElementById('write-shift').value) || 3);
        });
    }
    const shiftInput = document.getElementById('write-shift');
    if (shiftInput) shiftInput.addEventListener('input', () => updateShiftPreview(parseInt(shiftInput.value) || 1));
});

// ── SHARE CENTER ──────────────────────────────────────────────────────────────
let allUsers = [];
let selectedSendFile = '';
let selectedSendRecipient = '';

async function initShareCenter() {
    await loadShareUsers();
    populateFilePicker();
    switchShareTab('send');
    updateInboxBadge();
    renderShareStats();
}

async function loadShareUsers() {
    const res = await fetch(`/api/shares/users?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    allUsers = data.success ? data.users : [];
}

function renderShareStats() {
    fetch(`/api/shares/history?username=${encodeURIComponent(currentUser)}`)
        .then(r => r.json()).then(data => {
            const records = data.records || [];
            const sent = records.filter(r => r.sender === currentUser).length;
            const received = records.filter(r => r.recipient === currentUser).length;
            document.getElementById('share-stats-row').innerHTML = `
                <div class="stat-card">
                    <div class="stat-icon">📤</div>
                    <div class="stat-value" data-target="${sent}">0</div>
                    <div class="stat-label">Files Sent</div>
                </div>
                <div class="stat-card green">
                    <div class="stat-icon">📥</div>
                    <div class="stat-value" data-target="${received}">0</div>
                    <div class="stat-label">Files Received</div>
                </div>
                <div class="stat-card yellow">
                    <div class="stat-icon">👥</div>
                    <div class="stat-value" data-target="${allUsers.length}">0</div>
                    <div class="stat-label">Available Users</div>
                </div>`;
            animateCounters();
        });
}

function populateFilePicker() {
    const grid = document.getElementById('send-file-picker');
    if (!grid) return;
    if (allFiles.length === 0) {
        grid.innerHTML = `<div class="file-picker-empty">No files yet. Create some first.</div>`;
        return;
    }
    grid.innerHTML = allFiles.map(f => {
        const name = cleanFileName(f);
        return `<div class="file-picker-item" onclick="selectSendFile('${name}', this)">
            <div class="fp-icon">${getFileIcon(name)}</div>
            <div class="fp-name">${name}</div>
        </div>`;
    }).join('');
}

function selectSendFile(name, el) {
    document.querySelectorAll('.file-picker-item').forEach(i => i.classList.remove('selected'));
    el.classList.add('selected');
    selectedSendFile = name;
    document.getElementById('send-selected-file').value = name;
    const display = document.getElementById('send-selected-display');
    display.style.display = 'flex';
    display.innerHTML = `<span class="sfd-icon">${getFileIcon(name)}</span>
        <span class="sfd-name">${name}</span>
        <button class="sfd-clear" onclick="clearSendFile()">✕</button>`;
    updateSharePreview();
    checkSendReady();
}

function clearSendFile() {
    selectedSendFile = '';
    document.getElementById('send-selected-file').value = '';
    document.getElementById('send-selected-display').style.display = 'none';
    document.querySelectorAll('.file-picker-item').forEach(i => i.classList.remove('selected'));
    updateSharePreview();
    checkSendReady();
}

function filterRecipients(query) {
    const dropdown = document.getElementById('recipient-dropdown');
    const filtered = allUsers.filter(u =>
        u.username.toLowerCase().includes(query.toLowerCase()) && query.length > 0
    );
    if (filtered.length === 0) { dropdown.style.display = 'none'; return; }
    dropdown.style.display = 'block';
    dropdown.innerHTML = filtered.map(u => `
        <div class="recipient-option" onclick="selectRecipient('${u.username}', '${u.role}')">
            <div class="ro-avatar" style="background:${strColor(u.username)}">${u.username[0].toUpperCase()}</div>
            <div>
                <div class="ro-name">${u.username}</div>
                <div class="ro-role">${u.role}</div>
            </div>
        </div>`).join('');
}

function selectRecipient(username, role) {
    selectedSendRecipient = username;
    document.getElementById('send-selected-recipient').value = username;
    document.getElementById('send-recipient-input').value = '';
    document.getElementById('recipient-dropdown').style.display = 'none';
    const wrap = document.getElementById('send-recipient-display');
    wrap.style.display = 'block';
    wrap.innerHTML = `<div class="recipient-chip">
        <div class="rc-avatar" style="background:${strColor(username)}">${username[0].toUpperCase()}</div>
        <span class="rc-name">${username}</span>
        <span class="rc-role" style="font-size:11px;color:var(--text-dim);margin-left:2px;">(${role})</span>
        <button class="rc-clear" onclick="clearRecipient()">✕</button>
    </div>`;
    updateSharePreview();
    checkSendReady();
}

function clearRecipient() {
    selectedSendRecipient = '';
    document.getElementById('send-selected-recipient').value = '';
    document.getElementById('send-recipient-display').style.display = 'none';
    document.getElementById('send-recipient-display').innerHTML = '';
    updateSharePreview();
    checkSendReady();
}

function updateSharePreview() {
    const box = document.getElementById('share-preview-box');
    if (!selectedSendFile || !selectedSendRecipient) { box.style.display = 'none'; return; }
    box.style.display = 'block';
    document.getElementById('sp-from-avatar').textContent = currentUser[0].toUpperCase();
    document.getElementById('sp-from-avatar').style.background = strColor(currentUser);
    document.getElementById('sp-from-name').textContent = currentUser;
    document.getElementById('sp-file-icon').textContent = getFileIcon(selectedSendFile);
    document.getElementById('sp-file-name').textContent = selectedSendFile;
    document.getElementById('sp-to-avatar').textContent = selectedSendRecipient[0].toUpperCase();
    document.getElementById('sp-to-avatar').style.background = strColor(selectedSendRecipient);
    document.getElementById('sp-to-name').textContent = selectedSendRecipient;
}

function checkSendReady() {
    const btn = document.getElementById('btn-do-share');
    if (btn) btn.disabled = !(selectedSendFile && selectedSendRecipient);
}

async function doShare() {
    if (!selectedSendFile || !selectedSendRecipient) return;
    setLoading('btn-do-share', true);
    const res = await fetch('/api/files/share', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUser, fileName: selectedSendFile, recipient: selectedSendRecipient })
    });
    const data = await res.json();
    setLoading('btn-do-share', false);
    showMsg('share-center-msg', data.message, data.success ? 'success' : 'error');
    if (data.success) {
        logActivity(`Shared "${selectedSendFile}" → ${selectedSendRecipient}`, 'success');
        toast(`🔗 Sent to ${selectedSendRecipient}`, 'success');
        updateInboxBadge();
        renderShareStats();
        // reset form
        clearSendFile();
        clearRecipient();
        document.getElementById('share-preview-box').style.display = 'none';
        setTimeout(() => clearMsg('share-center-msg'), 3000);
    }
}

// ── SHARE TABS ────────────────────────────────────────────────────────────────
function switchShareTab(tab) {
    ['send','inbox','sent','history'].forEach(t => {
        document.getElementById(`stab-${t}`).classList.toggle('active', t === tab);
        document.getElementById(`share-pane-${t}`).style.display = t === tab ? 'block' : 'none';
    });
    if (tab === 'inbox') loadInbox();
    if (tab === 'sent') loadSent();
    if (tab === 'history') loadHistory();
}

async function loadInbox() {
    const container = document.getElementById('inbox-list');
    container.innerHTML = renderShareLoading();
    const res = await fetch(`/api/shares/inbox?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    renderShareRecords(container, data.records || [], 'received');
}

async function loadSent() {
    const container = document.getElementById('sent-list');
    container.innerHTML = renderShareLoading();
    const res = await fetch(`/api/shares/sent?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    renderShareRecords(container, data.records || [], 'sent');
}

async function loadHistory() {
    const container = document.getElementById('history-list');
    container.innerHTML = renderShareLoading();
    const res = await fetch(`/api/shares/history?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    renderShareRecords(container, data.records || [], 'all');
}

function renderShareRecords(container, records, mode) {
    if (records.length === 0) {
        container.innerHTML = `<div class="share-empty"><div class="se-icon">📭</div><p>Nothing here yet.</p></div>`;
        return;
    }
    container.innerHTML = records.map(r => {
        const isSent = r.sender === currentUser;
        const badgeClass = isSent ? 'sent' : 'received';
        const badgeText = isSent ? 'Sent' : 'Received';
        const otherUser = isSent ? r.recipient : r.sender;
        const dirLabel = isSent ? `To: ${r.recipient}` : `From: ${r.sender}`;
        return `<div class="share-record">
            <div class="sr-icon">${getFileIcon(r.fileName)}</div>
            <div class="sr-body">
                <div class="sr-title">${r.fileName}</div>
                <div class="sr-meta">
                    <span class="sr-meta-item">
                        <span class="user-row-avatar" style="background:${strColor(otherUser)};width:18px;height:18px;font-size:9px;display:inline-flex;">${otherUser[0].toUpperCase()}</span>
                        ${dirLabel}
                    </span>
                    <span class="sr-badge ${badgeClass}">${badgeText}</span>
                    <span class="sr-badge delivered">${r.status}</span>
                    <span class="sr-id">#${r.id}</span>
                </div>
            </div>
            <div class="sr-time">${r.timestamp}</div>
        </div>`;
    }).join('');
}

function renderShareLoading() {
    return `<div class="share-empty"><div class="se-icon">⏳</div><p>Loading...</p></div>`;
}

async function updateInboxBadge() {
    const res = await fetch(`/api/shares/inbox/count?username=${encodeURIComponent(currentUser)}`);
    const data = await res.json();
    const badge = document.getElementById('inbox-badge');
    if (!badge) return;
    if (data.count > 0) { badge.textContent = data.count; badge.style.display = 'inline'; }
    else badge.style.display = 'none';
}

// ── MODAL RECIPIENT SEARCH ────────────────────────────────────────────────────
let modalAllUsers = [];

async function loadModalRecipients() {
    if (modalAllUsers.length === 0) {
        const res = await fetch(`/api/shares/users?username=${encodeURIComponent(currentUser)}`);
        const data = await res.json();
        modalAllUsers = data.success ? data.users : [];
    }
}

function filterModalRecipients(query) {
    const dropdown = document.getElementById('modal-recipient-dropdown');
    const filtered = modalAllUsers.filter(u =>
        u.username.toLowerCase().includes(query.toLowerCase()) && query.length > 0
    );
    if (filtered.length === 0) { dropdown.style.display = 'none'; return; }
    dropdown.style.display = 'block';
    dropdown.innerHTML = filtered.map(u => `
        <div class="recipient-option" onclick="selectModalRecipient('${u.username}', '${u.role}')">
            <div class="ro-avatar" style="background:${strColor(u.username)}">${u.username[0].toUpperCase()}</div>
            <div>
                <div class="ro-name">${u.username}</div>
                <div class="ro-role">${u.role}</div>
            </div>
        </div>`).join('');
}

function selectModalRecipient(username, role) {
    document.getElementById('modal-selected-recipient').value = username;
    document.getElementById('modal-share-recipient').value = '';
    document.getElementById('modal-recipient-dropdown').style.display = 'none';
    const chip = document.getElementById('modal-recipient-chip');
    chip.style.display = 'block';
    chip.innerHTML = `<div class="recipient-chip">
        <div class="rc-avatar" style="background:${strColor(username)}">${username[0].toUpperCase()}</div>
        <span class="rc-name">${username}</span>
        <span style="font-size:11px;color:var(--text-dim);margin-left:2px;">(${role})</span>
        <button class="rc-clear" onclick="clearModalRecipient()">✕</button>
    </div>`;
}

function clearModalRecipient() {
    document.getElementById('modal-selected-recipient').value = '';
    document.getElementById('modal-recipient-chip').style.display = 'none';
    document.getElementById('modal-recipient-chip').innerHTML = '';
}

// close dropdowns when clicking outside
document.addEventListener('click', e => {
    if (!e.target.closest('.recipient-search-wrap')) {
        const d1 = document.getElementById('recipient-dropdown');
        const d2 = document.getElementById('modal-recipient-dropdown');
        if (d1) d1.style.display = 'none';
        if (d2) d2.style.display = 'none';
    }
});

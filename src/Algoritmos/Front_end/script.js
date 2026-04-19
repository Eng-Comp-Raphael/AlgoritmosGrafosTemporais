// Memória local do Front-end
let configData = { origem: '', destino: '', tAlpha: '', tOmega: '' };
let listaDeNos = [];
let listaDeArestas = [];

// ==========================================
// 1. CARREGAR DADOS DIRETAMENTE DO JAVA
// ==========================================
function carregarDoBanco(mostrarAviso = false) {
    fetch('http://localhost:8080/api/grafo')
        .then(resposta => {
            if (!resposta.ok) throw new Error("Servidor Java não está rodando!");
            return resposta.json(); 
        })
        .then(dadosDoJava => {
            configData.origem = (dadosDoJava.config.origem || '').toUpperCase();
            configData.destino = (dadosDoJava.config.destino || '').toUpperCase();
            configData.tAlpha = dadosDoJava.config.tAlpha;
            configData.tOmega = dadosDoJava.config.tOmega;

            listaDeNos = dadosDoJava.nos.map(no => no.toUpperCase());
            listaDeArestas = dadosDoJava.arestas.map(a => ({
                u: a.u.toUpperCase(), v: a.v.toUpperCase(), t: a.t, lambda: a.lambda
            }));

            document.getElementById('confOrigem').value = configData.origem;
            document.getElementById('confDestino').value = configData.destino;
            document.getElementById('confTAlpha').value = configData.tAlpha;
            document.getElementById('confTOmega').value = configData.tOmega;

            renderizarNos();
            renderizarArestas();
            atualizarResumo();

            if(mostrarAviso) alert("✅ Sincronizado com o Banco SQLite!");
        })
        .catch(erro => console.error(erro));
}

// Quando a página carrega, já puxa os dados silenciosamente!
window.onload = () => carregarDoBanco(false);


// ==========================================
// 2. NAVEGAÇÃO
// ==========================================
function mudarAba(idAba) {
    document.querySelectorAll('.tab-content').forEach(aba => aba.classList.remove('active'));
    document.querySelectorAll('.menu-item').forEach(btn => {
        if(!btn.classList.contains('highlight')) btn.classList.remove('active');
    });

    document.getElementById(idAba).classList.add('active');
    event.currentTarget.classList.add('active');
    if(idAba === 'aba-detalhes') atualizarResumo();
}


// ==========================================
// 3. SALVAR CONFIGURAÇÕES NO BANCO
// ==========================================
function salvarConfiguracoes() {
    const o = document.getElementById('confOrigem').value.toUpperCase();
    const d = document.getElementById('confDestino').value.toUpperCase();
    const ta = document.getElementById('confTAlpha').value;
    const to = document.getElementById('confTOmega').value;

    fetch('http://localhost:8080/api/config', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ origem: o, destino: d, tAlpha: ta, tOmega: to })
    }).then(() => {
        alert("✅ Configurações salvas diretamente no banco Java!");
        carregarDoBanco(false); // Sincroniza
    });
}


// ==========================================
// 4. GERENCIAR NÓS NO BANCO
// ==========================================
function adicionarNo() {
    const nome = document.getElementById('novoNo').value.trim().toUpperCase();
    if (nome === "") return alert("Digite um nome!");

    fetch('http://localhost:8080/api/no/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ no: nome })
    }).then(() => {
        document.getElementById('novoNo').value = "";
        carregarDoBanco(false); // Sincroniza a lista atualizada
    });
}

function deletarNo(nomeNo) {
    fetch('http://localhost:8080/api/no/delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ no: nomeNo })
    }).then(() => {
        carregarDoBanco(false); // Sincroniza após deletar
    });
}

function renderizarNos() {
    const ul = document.getElementById('listaNos');
    ul.innerHTML = ""; 
    if (listaDeNos.length === 0) return ul.innerHTML = '<li class="empty-msg">Nenhum nó cadastrado.</li>';

    listaDeNos.forEach(no => {
        ul.innerHTML += `<li><strong>Nó: ${no}</strong> <button class="btn-delete" onclick="deletarNo('${no}')">Deletar</button></li>`;
    });
}


// ==========================================
// 5. GERENCIAR ARESTAS NO BANCO
// ==========================================
function adicionarAresta() {
    const u = document.getElementById('arestaU').value.toUpperCase();
    const v = document.getElementById('arestaV').value.toUpperCase();
    const t = document.getElementById('arestaT').value;
    const l = document.getElementById('arestaLambda').value;

    if (!u || !v || !t || !l) return alert("Preencha tudo.");

    fetch('http://localhost:8080/api/aresta/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ u: u, v: v, t: t, lambda: l })
    }).then(() => {
        document.getElementById('arestaU').value = "";
        document.getElementById('arestaV').value = "";
        document.getElementById('arestaT').value = "";
        document.getElementById('arestaLambda').value = "";
        carregarDoBanco(false);
    });
}

function deletarAresta(index) {
    const aresta = listaDeArestas[index];
    fetch('http://localhost:8080/api/aresta/delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ u: aresta.u, v: aresta.v, t: aresta.t, lambda: aresta.lambda })
    }).then(() => {
        carregarDoBanco(false);
    });
}

function renderizarArestas() {
    const ul = document.getElementById('listaArestas');
    ul.innerHTML = "";
    if (listaDeArestas.length === 0) return ul.innerHTML = '<li class="empty-msg">Nenhuma aresta cadastrada.</li>';

    listaDeArestas.forEach((a, index) => {
        ul.innerHTML += `<li><span><strong>${a.u} ➡️ ${a.v}</strong> | Início: ${a.t}, Duração: ${a.lambda}</span> <button class="btn-delete" onclick="deletarAresta(${index})">Deletar</button></li>`;
    });
}


// ==========================================
// 6. RESUMO (DASHBOARD)
// ==========================================
function atualizarResumo() {
    document.getElementById('detalheRota').innerText = (configData.origem || '?') + " ➡️ " + (configData.destino || '?');
    document.getElementById('detalheTempo').innerText = (configData.tAlpha !== '' ? configData.tAlpha : '?') + " até " + (configData.tOmega !== '' ? configData.tOmega : '?');
    document.getElementById('detalheQtdNos').innerText = listaDeNos.length;
    document.getElementById('detalheQtdArestas').innerText = listaDeArestas.length;
}


// ==========================================
// 7. EXECUTAR ALGORITMO INTEGRADO
// ==========================================
function executarAlgoritmo() {
    document.getElementById('boxResultado').style.display = 'block';
    document.getElementById('resCaminho').innerText = "Calculando no Java...";
    document.getElementById('resTempo').innerText = "Calculando...";

    fetch('http://localhost:8080/api/executar')
        .then(r => r.json())
        .then(dados => {
            document.getElementById('resCaminho').innerText = dados.caminho;
            document.getElementById('resTempo').innerText = dados.tempo;
        })
        .catch(e => {
            document.getElementById('resCaminho').innerText = "Erro.";
            document.getElementById('resTempo').innerText = "Erro.";
        });
}
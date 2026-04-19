// Memória local do Front-end
let configData = { origem: '', destino: '', tAlpha: '', tOmega: '' };
let listaDeNos = [];
let listaDeArestas = [];
let network = null;
let caminhoVencedor = []; // Guarda o caminho para pintar de verde

// ==========================================
// 1. CARREGAR DADOS
// ==========================================
function carregarDoBanco(mostrarAviso = false) {
    fetch('http://localhost:8080/api/grafo')
        .then(resposta => resposta.json())
        .then(dados => {
            configData.origem = (dados.config.origem || '').toUpperCase();
            configData.destino = (dados.config.destino || '').toUpperCase();
            configData.tAlpha = dados.config.tAlpha;
            configData.tOmega = dados.config.tOmega;

            listaDeNos = dados.nos.map(n => n.toUpperCase());
            listaDeArestas = dados.arestas.map(a => ({
                u: a.u.toUpperCase(), v: a.v.toUpperCase(), t: a.t, lambda: a.lambda
            }));

            document.getElementById('confOrigem').value = configData.origem;
            document.getElementById('confDestino').value = configData.destino;
            document.getElementById('confTAlpha').value = configData.tAlpha;
            document.getElementById('confTOmega').value = configData.tOmega;

            renderizarNos();
            renderizarArestas();
            atualizarResumo();

            // Se estiver na aba do grafo, repinta na hora
            if (document.getElementById('aba-grafo').classList.contains('active')) {
                desenharGrafo();
            }

            if(mostrarAviso) alert("✅ Sincronizado!");
        }).catch(e => console.error("Erro na API", e));
}
window.onload = () => carregarDoBanco(false);

// ==========================================
// DESENHADOR DE GRAFOS (VIS.JS) - DRAG & STAY
// ==========================================
function desenharGrafo() {
    const container = document.getElementById('grafoContainer');
    if (!container || container.clientHeight === 0) return;

    const nodesVis = listaDeNos.map(no => {
        let corFundo = '#820ad1'; 
        let corBorda = '#6708a6';
        let larguraBorda = 1;

        if (caminhoVencedor.includes(no)) {
            corFundo = '#10b981'; // Verde (Caminho)
            corBorda = '#059669';
            larguraBorda = 4;
        } else if (no === configData.origem) {
            corFundo = '#3b82f6'; // Azul (Origem)
            corBorda = '#1d4ed8';
            larguraBorda = 3;
        } else if (no === configData.destino) {
            corFundo = '#f59e0b'; // Laranja (Destino)
            corBorda = '#b45309';
            larguraBorda = 3;
        }

        return {
            id: no, label: no,
            color: { background: corFundo, border: corBorda, highlight: corFundo },
            font: { color: 'white', size: 16, bold: true },
            borderWidth: larguraBorda,
            shadow: true
        };
    });

    const edgesVis = listaDeArestas.map(aresta => {
        let isCaminho = false;
        for (let i = 0; i < caminhoVencedor.length - 1; i++) {
            if (caminhoVencedor[i] === aresta.u && caminhoVencedor[i+1] === aresta.v) {
                isCaminho = true; break;
            }
        }

        return {
            from: aresta.u, to: aresta.v,
            label: `t:${aresta.t} | λ:${aresta.lambda}`,
            arrows: 'to',
            color: isCaminho ? { color: '#10b981', highlight: '#10b981' } : { color: '#cccccc' },
            width: isCaminho ? 4 : 1,
            font: { align: 'top', size: 12, color: isCaminho ? '#10b981' : '#888' }
        };
    });

    const data = { nodes: new vis.DataSet(nodesVis), edges: new vis.DataSet(edgesVis) };
    
    const options = {
        layout: {
            hierarchical: {
                enabled: true,
                direction: 'LR',
                sortMethod: 'directed',
                levelSeparation: 280,
                nodeSpacing: 150,
                edgeMinimization: true,
                blockShifting: true,
                parentCentralization: true
            }
        },
        edges: {
            smooth: {
                type: 'cubicBezier',
                forceDirection: 'horizontal',
                roundness: 0.5
            }
        },
        physics: {
            enabled: true, // Começa ligado para organizar
            hierarchicalRepulsion: {
                nodeDistance: 200,
                avoidOverlap: 1
            },
            stabilization: {
                enabled: true,
                iterations: 1000 // Organiza tudo antes de mostrar
            }
        },
        interaction: {
            dragNodes: true, // Permite arrastar
            dragView: true,
            zoomView: true
        }
    };

    if (network !== null) network.destroy();
    network = new vis.Network(container, data, options);

    // Assim que o grafo estabilizar o layout, desligamos a física para os nós ficarem onde soltar.
    network.once("stabilizationIterationsDone", function() {
        console.log("[Grafo] Layout estabilizado. Congelando posições para edição manual.");
        network.setOptions({ physics: false });
    });
}

// ==========================================
// NAVEGAÇÃO (CORRIGIDA À PROVA DE FALHAS)
// ==========================================
function mudarAba(idAba) {
    // Esconde todas as abas
    document.querySelectorAll('.tab-content').forEach(aba => aba.classList.remove('active'));
    
    // Varre todos os botões do menu lateral
    document.querySelectorAll('.menu-item').forEach(btn => {
        // Remove a seleção cinza de todos (ignorando o botão roxo)
        if(!btn.classList.contains('highlight')) btn.classList.remove('active');
        
        // Se o botão tiver o nome da aba atual no 'onclick', pinta ele de cinza
        if(btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(idAba)) {
            if(!btn.classList.contains('highlight')) btn.classList.add('active');
        }
    });

    // Mostra a aba escolhida na tela principal
    document.getElementById(idAba).classList.add('active');
    
    // Atualiza os resumos ou desenha o grafo, dependendo de onde entramos
    if(idAba === 'aba-detalhes') atualizarResumo();
    
    if(idAba === 'aba-grafo') {
        setTimeout(() => desenharGrafo(), 50);
    }
}

// ==========================================
// DEMAIS FUNÇÕES (Salvar, Add, Deletar)
// ==========================================
function salvarConfiguracoes() {
    const o = document.getElementById('confOrigem').value.toUpperCase();
    const d = document.getElementById('confDestino').value.toUpperCase();
    fetch('http://localhost:8080/api/config', {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ origem: o, destino: d, tAlpha: document.getElementById('confTAlpha').value, tOmega: document.getElementById('confTOmega').value })
    }).then(() => {
        alert("✅ Salvo!");
        caminhoVencedor = [];
        carregarDoBanco(false);
    });
}

function adicionarNo() {
    const nome = document.getElementById('novoNo').value.trim().toUpperCase();
    if (!nome) return;
    fetch('http://localhost:8080/api/no/add', {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: new URLSearchParams({ no: nome })
    }).then(() => { document.getElementById('novoNo').value = ""; carregarDoBanco(false); });
}

function deletarNo(nomeNo) {
    fetch('http://localhost:8080/api/no/delete', {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: new URLSearchParams({ no: nomeNo })
    }).then(() => carregarDoBanco(false));
}

function renderizarNos() {
    const ul = document.getElementById('listaNos'); ul.innerHTML = "";
    listaDeNos.forEach(no => ul.innerHTML += `<li><strong>Nó: ${no}</strong> <button class="btn-delete" onclick="deletarNo('${no}')">Deletar</button></li>`);
}

function adicionarAresta() {
    const u = document.getElementById('arestaU').value.toUpperCase(), v = document.getElementById('arestaV').value.toUpperCase();
    const t = document.getElementById('arestaT').value, l = document.getElementById('arestaLambda').value;
    if (!u || !v || !t || !l) return;
    fetch('http://localhost:8080/api/aresta/add', {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: new URLSearchParams({ u: u, v: v, t: t, lambda: l })
    }).then(() => {
        document.getElementById('arestaU').value = ""; document.getElementById('arestaV').value = "";
        document.getElementById('arestaT').value = ""; document.getElementById('arestaLambda').value = "";
        carregarDoBanco(false);
    });
}

function deletarAresta(index) {
    const aresta = listaDeArestas[index];
    fetch('http://localhost:8080/api/aresta/delete', {
        method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, body: new URLSearchParams({ u: aresta.u, v: aresta.v, t: aresta.t, lambda: aresta.lambda })
    }).then(() => carregarDoBanco(false));
}

function renderizarArestas() {
    const ul = document.getElementById('listaArestas'); ul.innerHTML = "";
    listaDeArestas.forEach((a, i) => ul.innerHTML += `<li><span><strong>${a.u} ➡️ ${a.v}</strong> | t: ${a.t}, λ: ${a.lambda}</span> <button class="btn-delete" onclick="deletarAresta(${i})">Deletar</button></li>`);
}

function atualizarResumo() {
    document.getElementById('detalheRota').innerText = (configData.origem || '?') + " ➡️ " + (configData.destino || '?');
    document.getElementById('detalheTempo').innerText = (configData.tAlpha !== '' ? configData.tAlpha : '?') + " até " + (configData.tOmega !== '' ? configData.tOmega : '?');
    document.getElementById('detalheQtdNos').innerText = listaDeNos.length;
    document.getElementById('detalheQtdArestas').innerText = listaDeArestas.length;
}

function executarAlgoritmo() {
    document.getElementById('boxResultado').style.display = 'block';
    document.getElementById('resCaminho').innerText = "Calculando...";
    document.getElementById('resTempo').innerText = "Calculando...";

    fetch('http://localhost:8080/api/executar')
        .then(r => r.json())
        .then(dados => {
            document.getElementById('resCaminho').innerText = dados.caminho;
            document.getElementById('resTempo').innerText = dados.tempo;

            if (dados.caminho !== "Inalcançável") {
                caminhoVencedor = dados.caminho.split(" -> ");
            } else {
                caminhoVencedor = [];
            }
            
            // Chama a nova função mudarAba de forma totalmente segura!
            setTimeout(() => mudarAba('aba-grafo'), 3000); 
        })
        .catch(e => console.error(e));
}
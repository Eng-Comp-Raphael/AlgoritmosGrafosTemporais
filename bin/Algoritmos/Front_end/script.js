// Memória local do Front-end (Simulando o Banco de Dados)
let configData = { origem: '', destino: '', tAlpha: '', tOmega: '' };
let listaDeNos = [];
let listaDeArestas = [];

// ==========================================
// 1. NAVEGAÇÃO ENTRE ABAS
// ==========================================
function mudarAba(idAba) {
    // Esconde todas as abas
    document.querySelectorAll('.tab-content').forEach(aba => {
        aba.classList.remove('active');
    });
    // Remove o 'active' de todos os botões de menu
    document.querySelectorAll('.menu-item').forEach(btn => {
        if(!btn.classList.contains('highlight')) {
            btn.classList.remove('active');
        }
    });

    // Mostra a aba clicada
    document.getElementById(idAba).classList.add('active');
    // Marca o botão clicado
    event.currentTarget.classList.add('active');

    // Se a pessoa clicou na aba Detalhes, atualizamos os dados lá
    if(idAba === 'aba-detalhes') atualizarResumo();
}

// ==========================================
// 2. SALVAR CONFIGURAÇÕES
// ==========================================
function salvarConfiguracoes() {
    configData.origem = document.getElementById('confOrigem').value.toUpperCase();
    configData.destino = document.getElementById('confDestino').value.toUpperCase();
    configData.tAlpha = document.getElementById('confTAlpha').value;
    configData.tOmega = document.getElementById('confTOmega').value;

    alert("Configurações salvas com sucesso!");
    // Futuro: Enviar configData via FETCH POST para o Java
}

// ==========================================
// 3. GERENCIAR NÓS
// ==========================================
function adicionarNo() {
    const inputNo = document.getElementById('novoNo');
    const nome = inputNo.value.trim().toUpperCase();

    if (nome === "") return alert("Digite um nome para o nó.");
    if (listaDeNos.includes(nome)) return alert("Nó já existe!");

    // Adiciona na memória e limpa campo
    listaDeNos.push(nome);
    listaDeNos.sort();
    inputNo.value = "";

    // Atualiza a tela
    renderizarNos();
    // Futuro: Enviar 'nome' via FETCH POST para o Java
}

function deletarNo(nomeNo) {
    // Remove do array
    listaDeNos = listaDeNos.filter(n => n !== nomeNo);
    // Atualiza a tela
    renderizarNos();
    // Futuro: Enviar 'nomeNo' via FETCH DELETE para o Java
}

function renderizarNos() {
    const ul = document.getElementById('listaNos');
    ul.innerHTML = ""; // Limpa a lista atual

    if (listaDeNos.length === 0) {
        ul.innerHTML = '<li class="empty-msg">Nenhum nó cadastrado.</li>';
        return;
    }

    listaDeNos.forEach(no => {
        ul.innerHTML += `
            <li>
                <strong>Nó: ${no}</strong>
                <button class="btn-delete" onclick="deletarNo('${no}')">Deletar</button>
            </li>
        `;
    });
}

// ==========================================
// 4. GERENCIAR ARESTAS
// ==========================================
function adicionarAresta() {
    const u = document.getElementById('arestaU').value.toUpperCase();
    const v = document.getElementById('arestaV').value.toUpperCase();
    const t = parseInt(document.getElementById('arestaT').value);
    const l = parseInt(document.getElementById('arestaLambda').value);

    if (!u || !v || isNaN(t) || isNaN(l)) return alert("Preencha todos os campos da aresta.");

    // Cria o objeto aresta e adiciona na lista
    const novaAresta = { u: u, v: v, t: t, lambda: l };
    listaDeArestas.push(novaAresta);
    
    // Ordena pelo tempo de início (t)
    listaDeArestas.sort((a, b) => a.t - b.t);

    // Limpa campos
    document.getElementById('arestaU').value = "";
    document.getElementById('arestaV').value = "";
    document.getElementById('arestaT').value = "";
    document.getElementById('arestaLambda').value = "";

    renderizarArestas();
}

function deletarAresta(index) {
    listaDeArestas.splice(index, 1); // Remove 1 item a partir do index
    renderizarArestas();
}

function renderizarArestas() {
    const ul = document.getElementById('listaArestas');
    ul.innerHTML = "";

    if (listaDeArestas.length === 0) {
        ul.innerHTML = '<li class="empty-msg">Nenhuma aresta cadastrada.</li>';
        return;
    }

    listaDeArestas.forEach((aresta, index) => {
        ul.innerHTML += `
            <li>
                <span><strong>${aresta.u} ➡️ ${aresta.v}</strong> | t: ${aresta.t}, λ: ${aresta.lambda}</span>
                <button class="btn-delete" onclick="deletarAresta(${index})">Deletar</button>
            </li>
        `;
    });
}

// ==========================================
// 5. RESUMO (DASHBOARD)
// ==========================================
function atualizarResumo() {
    document.getElementById('detalheRota').innerText = 
        (configData.origem || '?') + " ➡️ " + (configData.destino || '?');
        
    document.getElementById('detalheTempo').innerText = 
        (configData.tAlpha !== '' ? configData.tAlpha : '?') + " até " + (configData.tOmega !== '' ? configData.tOmega : '?');
        
    document.getElementById('detalheQtdNos').innerText = listaDeNos.length;
    document.getElementById('detalheQtdArestas').innerText = listaDeArestas.length;
}

// ==========================================
// 6. EXECUTAR ALGORITMO
// ==========================================
function executarAlgoritmo() {
    document.getElementById('boxResultado').style.display = 'block';
    
    // Mostrando que está carregando...
    document.getElementById('resCaminho').innerText = "Calculando...";
    document.getElementById('resTempo').innerText = "Calculando...";

    // Simulando tempo de processamento do Java (1 segundo)
    setTimeout(() => {
        document.getElementById('resCaminho').innerText = `${configData.origem || 'A'} -> B -> G -> ${configData.destino || 'K'}`;
        document.getElementById('resTempo').innerText = "7";
    }, 1000);
}
package grafo_com_matriz_de_adjacencia

import componentes_grafo.Aresta
import componentes_grafo.Grafo
import componentes_grafo.TipoGrafo
import componentes_grafo.Vertice


// Pode ser usado 0 em vez de null

class MatrizAdjacencia<T>(val tipoGrafo: TipoGrafo): Grafo<T> {
    private val vertices = arrayListOf<Vertice<T>>()
    override val todosVertices: ArrayList<Vertice<T>>
        get() = vertices
    // Matriz de adjacencia para guardar as arestas e pesos
    private val pesos = arrayListOf<ArrayList<Double?>>()

    override fun criarVertice(dado: T): Vertice<T> {
        val vertice = Vertice(vertices.count(), dado)
        vertices.add(vertice)
        // Adiciona zero em cada linha, pois nenhum vertice
        // atualmente possui uma aresta para o novo vértice
        pesos.forEach { peso ->
            peso.add(null)
        }
        // Cria um anova linha que vão guardar as arestas
        // que saem do vértice
        val linha = ArrayList<Double?>(vertices.count())
        repeat(vertices.count()) {
            linha.add(null)
        }
        pesos.add(linha)
        return vertice
    }

    // Adicionar arestas é adicionar elementos na matriz
    override fun adicionarArestaDirecionada(origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        pesos[origem.indice][destino.indice] = peso
    }

    // Procura arestas que estão saindo, ou seja, busca os pesos da linha
    override fun arestas(origem: Vertice<T>): ArrayList<Aresta<T>> {
        val arestas = arrayListOf<Aresta<T>>()
        (0 until pesos.size).forEach { coluna ->
            val peso = pesos[origem.indice][coluna]
            if (peso != null) {
                arestas.add(Aresta(origem, vertices[coluna], peso))
            }
        }
        return arestas
    }

    override fun peso(origem: Vertice<T>, destino: Vertice<T>): Double? {
        return pesos[origem.indice][destino.indice]
    }

    override fun adicionarArestaNaoDirecionada(origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        adicionarArestaDirecionada(origem, destino, peso)
        adicionarArestaDirecionada(destino, origem, peso)
    }

    override fun adicionar(origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        if (tipoGrafo == TipoGrafo.DIRECIONADO) {
            adicionarArestaDirecionada(origem, destino, peso)
        } else {
            adicionarArestaNaoDirecionada(origem, destino, peso)
        }
    }

    override fun toString(): String {
        val descricaoVertices = vertices
            .joinToString(separator = "\n") { "${it.indice}: ${it.dado}" }

        val matriz = pesos.map { linha ->
            buildString {
                (0 until pesos.size).forEach { indiceColuna ->
                    val valor = linha[indiceColuna]
                    if (valor != null) {
                        append("$valor\t")
                    } else {
                        append("ø\t\t")
                    }
                }
            }
        }

        val descricaoArestas = matriz.joinToString("\n")
        return "$descricaoVertices\n\n$descricaoArestas"
    }
}
package grafo_com_lista_adjacencia

import componentes_grafo.Aresta
import componentes_grafo.Grafo
import componentes_grafo.TipoAresta
import componentes_grafo.Vertice

class ListaAdjacencia<T>: Grafo<T> {

    private val adjacencias: HashMap<Vertice<T>, ArrayList<Aresta<T>>> = HashMap()
    override val todosVertices: ArrayList<Vertice<T>>
        get() = ArrayList(adjacencias.keys)
    val vertices: Set<Vertice<T>>
    // Para construir uma árvore geradora, é necessário ter acesso a todos
    // os vértices
    get() = adjacencias.keys

    // Copia todos os vértices para um novo grafo
    fun copiarVertices(grafo: ListaAdjacencia<T>) {
        grafo.vertices.forEach {vertice ->
            adjacencias[vertice] = arrayListOf()
        }
    }

    // Cria um vértice e o retorna
    override fun criarVertice(dado: T): Vertice<T> {
        val vertice = Vertice(adjacencias.count(), dado)
        // Na lista de adjacência, uma lista vazia de arestas é criada
        adjacencias[vertice] = ArrayList()
        return vertice
    }

    // Cria uma nova aresta e a adiciona na lista de adjacência
    override fun adicionarArestaDirecionada(origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        val aresta = Aresta(origem, destino, peso)
        adjacencias[origem]?.add(aresta)
    }

    // Uma aresta não direcionada pode ser percorrida nos dois sentidos.
    // Então, basta adicioná-las duas vezes, porém em sentidos diferentes
    override fun adicionarArestaNaoDirecionada(origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        adicionarArestaDirecionada(origem, destino, peso)
        adicionarArestaDirecionada(destino, origem, peso)
    }

    // Por meio da reutilizção dos métodos anteriores, cria uma aresta direcionada ou não
    override fun adicionar(aresta: TipoAresta, origem: Vertice<T>, destino: Vertice<T>, peso: Double?) {
        when (aresta) {
            TipoAresta.DIRECIONADO -> adicionarArestaDirecionada(origem, destino, peso)
            TipoAresta.NAO_DIRECIONADO -> adicionarArestaNaoDirecionada(origem, destino, peso)
        }
    }

    // Retorna todos as arestas de um vértice específico ou uma lista vazia se
    // o vértice especificado não existe
    // Todas as arestas relacionadas a um vértice estão saindo
    override fun arestas(origem: Vertice<T>): ArrayList<Aresta<T>> {
        return adjacencias[origem] ?: arrayListOf()
    }

    // Encontra o primeira aresta da fonte para o destino. Se essa aresta existir,
    // seu peso é retornado. Caso contrário, nulo é retornado
    override fun peso(origem: Vertice<T>, destino: Vertice<T>): Double? {
        return arestas(origem).firstOrNull { aresta ->
            aresta.destino == destino
        }?.peso
    }

    // Método utilizado apenas para imprimir a lista de adjacência
    override fun toString(): String {
        return buildString {
            adjacencias.forEach { (vertice, arestas) ->
                val stringAresta = arestas.joinToString { aresta ->
                    aresta.destino.dado.toString()
                }
                append("${vertice.dado} ---> [ $stringAresta ]\n")
            }
        }
    }
}
package doceria.backend.benditodoce.services;

import doceria.backend.benditodoce.domain.*;
import doceria.backend.benditodoce.domain.enums.EstadoPagamento;
import doceria.backend.benditodoce.domain.enums.Perfil;
import doceria.backend.benditodoce.domain.enums.TipoCliente;
import doceria.backend.benditodoce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class DBService {

    @Autowired
    private BCryptPasswordEncoder pe;

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    public void instantiateTestDatabase() throws ParseException {

        List<Categoria> categorias = createCategorias();
        List<Produto> produtos = createProdutos();

        assignProdutosToCategorias(categorias, produtos);

        categoriaRepository.save(categorias);
        produtoRepository.save(produtos);

        List<Estado> estados = createEstados();
        List<Cidade> cidades = createCidades(estados);

        estadoRepository.save(estados);
        cidadeRepository.save(cidades);

        List<Cliente> clientes = createClientes();
        List<Endereco> enderecos = createEnderecos(clientes, cidades);

        clienteRepository.save(clientes);
        enderecoRepository.save(enderecos);

        List<Pedido> pedidos = createPedidos(clientes, enderecos);
        List<Pagamento> pagamentos = createPagamentos(pedidos);

        pedidoRepository.save(pedidos);
        pagamentoRepository.save(pagamentos);

        List<ItemPedido> itensPedido = createItensPedido(pedidos, produtos);

        itemPedidoRepository.save(itensPedido);
    }

    private List<Categoria> createCategorias() {
        return Stream.of(
                new Categoria(null, "Bolos"),
                new Categoria(null, "Tortas"),
                new Categoria(null, "Doces"),
                new Categoria(null, "Salgados"),
                new Categoria(null, "Bebidas"),
                new Categoria(null, "Confeitaria"),
                new Categoria(null, "Pães")
        ).collect(Collectors.toList());
    }

    private List<Produto> createProdutos() {
        List<Produto> produtos = Stream.of(
                new Produto(null, "Bolo de Chocolate", 30.00),
                new Produto(null, "Torta de Limão", 25.00),
                new Produto(null, "Brigadeiro", 2.00),
                new Produto(null, "Coxinha", 5.00),
                new Produto(null, "Suco de Laranja", 8.00),
                new Produto(null, "Cupcake", 6.00),
                new Produto(null, "Pão Francês", 0.50),
                new Produto(null, "Palha Italiana", 10.0)
        ).collect(Collectors.toList());

        produtos.addAll(createMoreProdutos());

        return produtos;
    }

    private List<Produto> createMoreProdutos() {
        return IntStream.rangeClosed(8, 30)
                .mapToObj(i -> new Produto(null, "Produto " + i, 10.00))
                .collect(Collectors.toList());
    }

    private void assignProdutosToCategorias(List<Categoria> categorias, List<Produto> produtos) {
        categorias.get(0).getProdutos().addAll(Arrays.asList(produtos.get(0)));
        categorias.get(1).getProdutos().addAll(Arrays.asList(produtos.get(1)));
        categorias.get(2).getProdutos().addAll(Arrays.asList(produtos.get(2), produtos.get(5)));
        categorias.get(3).getProdutos().addAll(Arrays.asList(produtos.get(3)));
        categorias.get(4).getProdutos().addAll(Arrays.asList(produtos.get(4)));
        categorias.get(5).getProdutos().addAll(Arrays.asList(produtos.get(5)));
        categorias.get(6).getProdutos().addAll(Arrays.asList(produtos.get(6)));

        produtos.get(0).getCategorias().add(categorias.get(0));
        produtos.get(1).getCategorias().add(categorias.get(1));
        produtos.get(2).getCategorias().add(categorias.get(2));
        produtos.get(3).getCategorias().add(categorias.get(3));
        produtos.get(4).getCategorias().add(categorias.get(4));
        produtos.get(5).getCategorias().add(categorias.get(5));
        produtos.get(6).getCategorias().add(categorias.get(6));

        IntStream.rangeClosed(7, 29).forEach(i -> {
            categorias.get(2).getProdutos().add(produtos.get(i));
            produtos.get(i).getCategorias().add(categorias.get(2));
        });
    }

    private List<Estado> createEstados() {
        return Stream.of(
                new Estado(null, "Acre"),
                new Estado(null, "Alagoas"),
                new Estado(null, "Amapá"),
                new Estado(null, "Amazonas"),
                new Estado(null, "Bahia"),
                new Estado(null, "Ceará"),
                new Estado(null, "Distrito Federal"),
                new Estado(null, "Espírito Santo"),
                new Estado(null, "Goiás"),
                new Estado(null, "Maranhão"),
                new Estado(null, "Mato Grosso"),
                new Estado(null, "Mato Grosso do Sul"),
                new Estado(null, "Minas Gerais"),
                new Estado(null, "Pará"),
                new Estado(null, "Paraíba"),
                new Estado(null, "Paraná"),
                new Estado(null, "Pernambuco"),
                new Estado(null, "Piauí"),
                new Estado(null, "Rio de Janeiro"),
                new Estado(null, "Rio Grande do Norte"),
                new Estado(null, "Rio Grande do Sul"),
                new Estado(null, "Rondônia"),
                new Estado(null, "Roraima"),
                new Estado(null, "Santa Catarina"),
                new Estado(null, "São Paulo"),
                new Estado(null, "Sergipe"),
                new Estado(null, "Tocantins")
        ).collect(Collectors.toList());
    }

    private List<Cidade> createCidades(List<Estado> estados) {
        Estado ac = estados.get(0);
        Estado al = estados.get(1);
        Estado ap = estados.get(2);
        Estado am = estados.get(3);
        Estado ba = estados.get(4);
        Estado ce = estados.get(5);
        Estado df = estados.get(6);
        Estado es = estados.get(7);
        Estado go = estados.get(8);
        Estado ma = estados.get(9);
        Estado mt = estados.get(10);
        Estado ms = estados.get(11);
        Estado mg = estados.get(12);
        Estado pa = estados.get(13);
        Estado pb = estados.get(14);
        Estado pr = estados.get(15);
        Estado pe = estados.get(16);
        Estado pi = estados.get(17);
        Estado rj = estados.get(18);
        Estado rn = estados.get(19);
        Estado rs = estados.get(20);
        Estado ro = estados.get(21);
        Estado rr = estados.get(22);
        Estado sc = estados.get(23);
        Estado sp = estados.get(24);
        Estado se = estados.get(25);
        Estado to = estados.get(26);

        Cidade c1 = new Cidade(null, "Rio Branco", ac);
        Cidade c2 = new Cidade(null, "Maceió", al);
        Cidade c3 = new Cidade(null, "Macapá", ap);
        Cidade c4 = new Cidade(null, "Manaus", am);
        Cidade c5 = new Cidade(null, "Salvador", ba);
        Cidade c6 = new Cidade(null, "Fortaleza", ce);
        Cidade c7 = new Cidade(null, "Brasília", df);
        Cidade c8 = new Cidade(null, "Vitória", es);
        Cidade c9 = new Cidade(null, "Goiânia", go);
        Cidade c10 = new Cidade(null, "São Luís", ma);
        Cidade c11 = new Cidade(null, "Cuiabá", mt);
        Cidade c12 = new Cidade(null, "Campo Grande", ms);
        Cidade c13 = new Cidade(null, "Belo Horizonte", mg);
        Cidade c14 = new Cidade(null, "Belém", pa);
        Cidade c15 = new Cidade(null, "João Pessoa", pb);
        Cidade c16 = new Cidade(null, "Curitiba", pr);
        Cidade c17 = new Cidade(null, "Recife", pe);
        Cidade c18 = new Cidade(null, "Teresina", pi);
        Cidade c19 = new Cidade(null, "Rio de Janeiro", rj);
        Cidade c20 = new Cidade(null, "Natal", rn);
        Cidade c21 = new Cidade(null, "Porto Alegre", rs);
        Cidade c22 = new Cidade(null, "Porto Velho", ro);
        Cidade c23 = new Cidade(null, "Boa Vista", rr);
        Cidade c24 = new Cidade(null, "Florianópolis", sc);
        Cidade c25 = new Cidade(null, "São Paulo", sp);
        Cidade c26 = new Cidade(null, "Aracaju", se);
        Cidade c27 = new Cidade(null, "Palmas", to);

        ac.getCidades().add(c1);
        al.getCidades().add(c2);
        ap.getCidades().add(c3);
        am.getCidades().add(c4);
        ba.getCidades().add(c5);
        ce.getCidades().add(c6);
        df.getCidades().add(c7);
        es.getCidades().add(c8);
        go.getCidades().add(c9);
        ma.getCidades().add(c10);
        mt.getCidades().add(c11);
        ms.getCidades().add(c12);
        mg.getCidades().add(c13);
        pa.getCidades().add(c14);
        pb.getCidades().add(c15);
        pr.getCidades().add(c16);
        pe.getCidades().add(c17);
        pi.getCidades().add(c18);
        rj.getCidades().add(c19);
        rn.getCidades().add(c20);
        rs.getCidades().add(c21);
        ro.getCidades().add(c22);
        rr.getCidades().add(c23);
        sc.getCidades().add(c24);
        sp.getCidades().add(c25);
        se.getCidades().add(c26);
        to.getCidades().add(c27);

        return Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20, c21, c22, c23, c24, c25, c26, c27);
    }


    private List<Cliente> createClientes() {
        Cliente cli1 = new Cliente(null, "Maria Silva", "maria.silva@gmail.com", "36378912377", TipoCliente.PESSOAFISICA, pe.encode("123"));
        cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

        Cliente cli2 = new Cliente(null, "Luan", "luan@gmail.com", "31628382740", TipoCliente.PESSOAFISICA, pe.encode("123"));
        cli2.getTelefones().addAll(Arrays.asList("93883321", "34252625"));
        cli2.addPerfil(Perfil.ADMIN);

        Cliente cli3 = new Cliente(null, "Dicyane Medeiros", "dicyane@gmail.com", "61928382740", TipoCliente.PESSOAFISICA, pe.encode("123"));
        cli3.getTelefones().addAll(Arrays.asList("93883322", "34252626"));
        cli3.addPerfil(Perfil.ADMIN);

        return Arrays.asList(cli1, cli2, cli3);
    }

    private List<Endereco> createEnderecos(List<Cliente> clientes, List<Cidade> cidades) {
        Cliente cli1 = clientes.get(0);
        Cliente cli2 = clientes.get(1);
        Cliente cli3 = clientes.get(2);

        Cidade c1 = cidades.get(0);
        Cidade c2 = cidades.get(1);

        Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
        Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
        Endereco e3 = new Endereco(null, "Avenida Floriano", "2106", null, "Centro", "281777012", cli2, c2);
        Endereco e4 = new Endereco(null, "Rua das Palmeiras", "123", null, "Centro", "291777013", cli3, c2);

        cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
        cli2.getEnderecos().addAll(Arrays.asList(e3));
        cli3.getEnderecos().addAll(Arrays.asList(e4));

        return Arrays.asList(e1, e2, e3, e4);
    }

    private List<Pedido> createPedidos(List<Cliente> clientes, List<Endereco> enderecos) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Cliente cli1 = clientes.get(0);

        Endereco e1 = enderecos.get(0);
        Endereco e2 = enderecos.get(1);

        Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
        Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);

        cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

        return Arrays.asList(ped1, ped2);
    }

    private List<Pagamento> createPagamentos(List<Pedido> pedidos) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Pedido ped1 = pedidos.get(0);
        Pedido ped2 = pedidos.get(1);

        Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
        ped1.setPagamento(pagto1);

        Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
        ped2.setPagamento(pagto2);

        return Arrays.asList(pagto1, pagto2);
    }

    private List<ItemPedido> createItensPedido(List<Pedido> pedidos, List<Produto> produtos) {
        Pedido ped1 = pedidos.get(0);
        Pedido ped2 = pedidos.get(1);

        Produto p1 = produtos.get(0);
        Produto p2 = produtos.get(1);
        Produto p3 = produtos.get(2);

        ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 30.00);
        ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 2.00);
        ItemPedido ip3 = new ItemPedido(ped2, p2, 0.00, 1, 25.00);

        ped1.getItens().addAll(Arrays.asList(ip1, ip2));
        ped2.getItens().addAll(Arrays.asList(ip3));

        p1.getItens().addAll(Arrays.asList(ip1));
        p2.getItens().addAll(Arrays.asList(ip3));
        p3.getItens().addAll(Arrays.asList(ip2));

        return Arrays.asList(ip1, ip2, ip3);
    }
}

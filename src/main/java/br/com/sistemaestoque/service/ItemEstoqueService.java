package br.com.sistemaestoque.service;

import br.com.sistemaestoque.dao.ItemEstoqueDAO;
import br.com.sistemaestoque.dao.ProdutoDAO;
import br.com.sistemaestoque.dao.SaidaDAO;
import br.com.sistemaestoque.models.ItemEstoque;
import br.com.sistemaestoque.models.Produto;
import br.com.sistemaestoque.models.Saida;
import org.python.util.PythonInterpreter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/estoque")
public class ItemEstoqueService {

    @PutMapping
    @ResponseBody
    public ItemEstoque salvar(@RequestBody ItemEstoque itemEstoque){

        new ItemEstoqueDAO().save(itemEstoque);

        return itemEstoque;
    }

    @PostMapping("findAll")
    @ResponseBody
    public List<ItemEstoque> findAll(@RequestBody ItemEstoque filter) {
        ArrayList<ItemEstoque> out = new ArrayList<>();

        ItemEstoque itemEstoque;
        for(Produto produto : new ProdutoDAO().findAll(filter.getProduto() == null ? new Produto() : filter.getProduto())){

            itemEstoque = new ItemEstoqueDAO().findByProduto(produto);

            if(itemEstoque == null){
                itemEstoque = new ItemEstoque(produto);
            }

            out.add(itemEstoque);
        }

        return out;
    }

    @GetMapping("findById")
    @ResponseBody
    public ItemEstoque findById(@RequestParam int id) {
        return new ItemEstoqueDAO().findById(id);
    }

    @DeleteMapping
    @ResponseBody
    public ItemEstoque remover(@RequestBody ItemEstoque itemEstoque) {
        return new ItemEstoqueDAO().remover(itemEstoque);
    }

    @GetMapping("findByProduto")
    @ResponseBody
    public ItemEstoque findByProduto(@RequestParam int idProduto) {
        ItemEstoque out =  new ItemEstoqueDAO().findByProduto(new Produto(idProduto));

        if(out == null) {
            out = new ItemEstoque(new ProdutoDAO().findById(idProduto));
        }
        return out;
    }

    @PostMapping("calcularEstoque")
    @ResponseBody
    public void calcularEstoque() {
        // pegar todas as saidas desde 5 anos atras
        LocalDate localDate = LocalDate.now()
                .minusYears(5l);
        List<Saida> saidas = new SaidaDAO().findAfter(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        List<Produto> produtos = new ProdutoDAO().findAllIgnoraAtivo(new Produto());

        // separar por produto
        HashMap<Produto, List<Saida>> produtoSaidaListMap = new HashMap<>();
        List<Saida> saidasMes;
        for( Produto produto : produtos ){

            saidasMes = saidas.stream().filter(saida -> saida.getProduto().getId() == produto.getId()).collect(Collectors.toList());

            produtoSaidaListMap.put(produto, saidasMes);
        }

        // para cada produto...
        List<Saida> saidasNoMes;
        for(Map.Entry<Produto, List<Saida>> entry : produtoSaidaListMap.entrySet()) {

            // pegar as saidas referente ao mes atual
            saidasNoMes = entry.getValue().stream()
                    .filter(saida -> saida.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonth().equals(LocalDate.now().getMonth()))
                    .collect(Collectors.toList());

            // calcular a media de 5 anos
            // somar as vendas do mes de cada ano
            HashMap<Integer, Integer> anoSomaVendasMap = new HashMap<>();
            for (Saida saida : saidasNoMes ) {
                int ano = saida.getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
                Integer somaDoMesAno = anoSomaVendasMap.get(ano);
                if(somaDoMesAno == null)
                    somaDoMesAno = 0;

                somaDoMesAno += saida.getQuantidade();

                anoSomaVendasMap.put(ano, somaDoMesAno);

            }

            // tirar a media dos 5 anos
            int soma = 0;
            for(int qt : anoSomaVendasMap.values())
                soma += qt;

            // se não tiver os 5 anos, ira considerar a quantidade de anos que tiver
            int media = anoSomaVendasMap.values().size() == 0 ? 0 : soma / Math.min(5, anoSomaVendasMap.values().size());

            // buscar as quantidade de busca no mês anterior no Google Trends
            List<Integer> valores = new ArrayList<>();
            System.out.println("------------------------------");
            try {
                valores = buscarGoogleTrends(
                        LocalDate.now().minusMonths(1).withDayOfMonth(1),
                        LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()),
                        "Tecido+"
                                + entry.getKey().getNome().trim().replace(' ', '+')
                                + "+" + entry.getKey().getCorPredominante().trim().replace(' ', '+')
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("------------------------------");

            // calcular a quantidade ideal baseado no Google Trends. obs: considera 3% a taxa de conversão
            soma = 0;
            for(Integer i : valores) soma += i;
            int estoqueIdelGoogleTrends = (int)(soma * 0.03);

            // aplica essa media na quantidade ideal no estoque
            ItemEstoque itemEstoque = new ItemEstoqueDAO().findByProduto(entry.getKey());
            if(itemEstoque == null)
                itemEstoque = new ItemEstoque(entry.getKey());

            itemEstoque.setEstoqueIdeal(Math.max(media, estoqueIdelGoogleTrends));

            new ItemEstoqueDAO().save(itemEstoque);

        }

    }

    private List<Integer> buscarGoogleTrends(LocalDate inicio, LocalDate fim, String kw) throws IOException {

        ArrayList<Integer> out = new ArrayList<>();

        File file = new ClassPathResource("ConsultaGoogleTrends.py", this.getClass().getClassLoader()).getFile();
        System.out.println(file.getAbsolutePath());

        Process process = Runtime.getRuntime().exec("python " + file.getAbsolutePath() + " " + inicio + " " + fim + " " + kw);


        ArrayList<String> output = new ArrayList<>();
        try(Scanner scan = new Scanner(process.getInputStream())){
            System.out.println("Output: ");
            String outLine;
            while(scan.hasNext()) {
                outLine = scan.nextLine();
                output.add(outLine);
                System.out.println(outLine);
            }
        }

        try(Scanner err = new Scanner(process.getInputStream())) {
            while(err.hasNext())
                System.out.println(err.nextLine());
        }

        for (String line : output) {
            if(line.contains("-")) {
                List<String> infos = Arrays.stream(line.split(" ")).filter(str -> !str.isEmpty() ).collect(Collectors.toList());
                out.add(Integer.parseInt(infos.get(1)));
            }
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {

        }

        process.destroy();


        return out;
    }


}

package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import model.beans.Album;
import model.beans.Boleto;
import model.beans.Carrinho;
import model.beans.Cartao;
import model.beans.Filme;
import model.beans.Jogo;
import model.beans.Midia;
import model.beans.Pagamento;
import model.beans.Pix;
import model.dao.AlbumDAO;
import model.dao.FilmeDAO;
import model.dao.JogoDAO;
import model.dao.ListagemDAO;
import model.dao.MidiaDAO;
import model.dao.UsuarioDAO;
import model.beans.Usuario;
import java.util.List;
import java.util.ArrayList;

public class TelaInicialCliente extends JFrame {
    private Usuario usuario;
 
    public Usuario getUsuario() {
        return usuario;
    }
 
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
 
    public TelaInicialCliente() {
 
        MidiaDAO midiaDAO = new MidiaDAO();
 
        setTitle("Tela Inicial - Galaxy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);
 
        // ── Painel principal ──────────────────────────────────────────────
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
 
        // ── Cabeçalho ────────────────────────────────────────────────────
        JPanel headerPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
 
        // Faixa roxa com título
        Panel titleBand = new Panel();
        titleBand.setBackground(new Color(128, 128, 192));
        JLabel titulo = new JLabel("GALAXY");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleBand.add(titulo);
        headerPanel.add(titleBand, BorderLayout.NORTH);
 
        // Barra de ferramentas
        JToolBar optionsToolBar = new JToolBar();
        optionsToolBar.setRollover(true);
        optionsToolBar.setFloatable(false);
 
        JButton midiaButton = new JButton("Ver Mídias");
        midiaButton.addActionListener(e -> exibirOpcoesDeListagem());
        optionsToolBar.add(midiaButton);
 
        JButton perfilButton = new JButton("Perfil");
        perfilButton.addActionListener(e -> exibirOpcoesDePerfil());
        optionsToolBar.add(perfilButton);
 
        // ── BOTÃO SAIR ────────────────────────────────────────────────────
        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(e -> {
            dispose();
            Login login = new Login();
            login.setVisible(true);
        });
        optionsToolBar.add(sairButton);
 
        headerPanel.add(optionsToolBar, BorderLayout.WEST);
 
     // ── Barra de busca com botão ──────────────────────────────────────────
        JPanel searchPanel = new JPanel(new BorderLayout(4, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 8));

        JTextField searchField = new JTextField();
        searchField.setColumns(20);
        searchField.setToolTipText("Digite o nome da mídia e pressione Buscar ou Enter");

        JButton searchButton = new JButton("🔍 Buscar");
        searchButton.setBackground(new Color(128, 128, 192));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchButton.setFocusPainted(false);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
 
        // ── Área central: scroll com cards de todas as mídias ────────────
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
 
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
 
        // Carrega e exibe todas as mídias em cards
        List<Midia> todasMidias = midiaDAO.listarTodasMidias();
        for (Midia m : todasMidias) {
            listaPanel.add(criarCard(m));
        }
 
        // ── Botão Carrinho no rodapé ──────────────────────────────────────
        JButton cartButton = new JButton("🛒  Carrinho");
        cartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartButton.setBackground(new Color(128, 128, 192));
        cartButton.setForeground(Color.black);
        cartButton.setPreferredSize(new Dimension(0, 40));
        mainPanel.add(cartButton, BorderLayout.SOUTH);
 
        cartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String quantidadeStr = JOptionPane.showInputDialog(
                        TelaInicialCliente.this, "Quantos itens deseja adicionar ao carrinho?");
                if (quantidadeStr == null || quantidadeStr.isEmpty()) return;
 
                int quantidade;
                try {
                    quantidade = Integer.parseInt(quantidadeStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TelaInicialCliente.this,
                            "Digite um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                List<Integer> idsSelecionados = new ArrayList<>();
                for (int i = 0; i < quantidade; i++) {
                    String id = JOptionPane.showInputDialog(
                            TelaInicialCliente.this, "Digite o ID da " + (i + 1) + "ª mídia:");
                    if (id == null || id.isEmpty()) break;
                    try {
                        idsSelecionados.add(Integer.parseInt(id));
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(TelaInicialCliente.this,
                                "ID inválido, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                        i--;
                    }
                }
 
                if (idsSelecionados.isEmpty()) return;
 
                // ── Exibe os itens em um JDialog separado (NÃO usa mainPanel) ──
                JPanel previewPanel = new JPanel();
                previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
                midiaDAO.exibirInformacoesMidiasSelecionadas(idsSelecionados, previewPanel);
 
                JScrollPane previewScroll = new JScrollPane(previewPanel);
                previewScroll.setPreferredSize(new Dimension(500, 350));
 
                int opcao = JOptionPane.showConfirmDialog(
                        TelaInicialCliente.this, previewScroll, "Itens selecionados — Deseja comprar?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
 
                if (opcao == JOptionPane.YES_OPTION) {
                    Carrinho carrinho = new Carrinho();
                    for (Integer id : idsSelecionados) {
                        Midia midia = MidiaDAO.obterMidiaPorId(id);
                        if (midia != null) carrinho.adicionarItem(midia);
                    }
 
                    int opcao2 = JOptionPane.showConfirmDialog(
                            TelaInicialCliente.this,
                            "Total da compra: R$ " + String.format("%.2f", carrinho.getTotalCompra())
                                    + "\nDeseja finalizar a compra?");
                    if (opcao2 == JOptionPane.YES_OPTION) {
                        exibirOpcoesDePagamento(carrinho);
                    }
                }
            }
        });
 
     // Ação compartilhada entre Enter e o botão Buscar
        ActionListener buscaAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = searchField.getText().trim();
                if (!nome.isEmpty()) {
                    ListagemFrame<Midia> listagemFrame = new ListagemFrame<>("Resultado da Busca",
                        new ListagemDAO<Midia>() {
                            @Override
                            public void listar(JPanel mainPanel) {
                                midiaDAO.consultarMidia(nome, mainPanel);
                            }
                        });
                    listagemFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(TelaInicialCliente.this, "Digite um nome para pesquisar.");
                }
            }
        };
        searchField.addActionListener(buscaAction);  // Enter ainda funciona
        searchButton.addActionListener(buscaAction); // Botão também funciona
    }
 
    // ── Cria um card visual para cada mídia ───────────────────────────────
    private JPanel criarCard(Midia m) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(6, 10, 6, 10),
                BorderFactory.createLineBorder(new Color(200, 200, 220))));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setBackground(Color.WHITE);
 
        // Imagem redimensionada
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(100, 130));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL url = new URL(m.getCapa());
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            imgLabel.setText("Sem foto");
            imgLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        }
        card.add(imgLabel, BorderLayout.WEST);
 
        // Informações
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 10));
 
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
 
        JLabel nomeLabel = new JLabel(m.getNome());
        nomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
 
        info.add(nomeLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(makeLabel("ID: " + m.getIdMidia(), labelFont));
        info.add(makeLabel("Tipo: " + m.getTipo(), labelFont));
        info.add(makeLabel("Preço: R$ " + String.format("%.2f", m.getPreco()), labelFont));
        info.add(makeLabel("Classificação: " + m.getClassIndicativa(), labelFont));
        info.add(makeLabel("Gênero: " + m.getGenero(), labelFont));
        info.add(makeLabel("Estoque: " + m.getQtdEstoque(), labelFont));
 
        card.add(info, BorderLayout.CENTER);
        return card;
    }
 
    private JLabel makeLabel(String text, Font font) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        return l;
    }
 
    // ── Listagem por tipo ──────────────────────────────────────────────────
    private void exibirOpcoesDeListagem() {
        JPanel panel = new JPanel(new BorderLayout());
        ButtonGroup group = new ButtonGroup();
 
        JRadioButton filmeRB = new JRadioButton("Filme");
        JRadioButton albumRB = new JRadioButton("Album");
        JRadioButton jogoRB  = new JRadioButton("Jogo");
        group.add(filmeRB); group.add(albumRB); group.add(jogoRB);
        panel.add(filmeRB, BorderLayout.NORTH);
        panel.add(albumRB, BorderLayout.CENTER);
        panel.add(jogoRB,  BorderLayout.SOUTH);
 
        JOptionPane.showMessageDialog(this, panel, "Escolha o tipo de mídia", JOptionPane.PLAIN_MESSAGE);
 
        if (filmeRB.isSelected()) {
            new ListagemFrame<Filme>("Listagem de Filmes", new FilmeDAO()).setVisible(true);
        } else if (albumRB.isSelected()) {
            new ListagemFrame<Album>("Listagem de Álbuns", new AlbumDAO()).setVisible(true);
        } else if (jogoRB.isSelected()) {
            new ListagemFrame<Jogo>("Listagem de Jogos", new JogoDAO()).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma opção!");
        }
    }
 
    // ── Perfil ─────────────────────────────────────────────────────────────
    private void exibirOpcoesDePerfil() {
        JPanel panel = new JPanel(new BorderLayout());
        ButtonGroup group = new ButtonGroup();
 
        JRadioButton atualizarRB = new JRadioButton("Atualizar Perfil");
        JRadioButton apagarRB   = new JRadioButton("Apagar Perfil");
        group.add(atualizarRB); group.add(apagarRB);
        panel.add(atualizarRB, BorderLayout.NORTH);
        panel.add(apagarRB,   BorderLayout.CENTER);
 
        JOptionPane.showMessageDialog(this, panel, "Escolha uma opção:", JOptionPane.PLAIN_MESSAGE);
 
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (atualizarRB.isSelected()) {
            usuarioDAO.alterarUsuario(this.usuario.getId());
        } else if (apagarRB.isSelected()) {
            int opcao = JOptionPane.showConfirmDialog(null,
                    "Deseja excluir o seu perfil?\n\nEssa ação não pode ser desfeita.");
            if (opcao == JOptionPane.YES_OPTION) {
                usuarioDAO.apagarUsuario(this.usuario.getId());
                JOptionPane.showMessageDialog(null, "Perfil apagado com sucesso");
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma opção!");
        }
    }
 
    // ── Pagamento ──────────────────────────────────────────────────────────
    public void exibirOpcoesDePagamento(Carrinho carrinho) {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        ButtonGroup group = new ButtonGroup();
 
        JRadioButton boletoRB  = new JRadioButton("Boleto");
        JRadioButton cartaoRB  = new JRadioButton("Cartão");
        JRadioButton pixRB     = new JRadioButton("Pix");
        group.add(boletoRB); group.add(cartaoRB); group.add(pixRB);
        panel.add(boletoRB); panel.add(cartaoRB); panel.add(pixRB);
 
        int result = JOptionPane.showConfirmDialog(null, panel, "Escolha a forma de pagamento:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
 
        Pagamento pagamento = null;
        try {
            if (boletoRB.isSelected()) {
                JTextField codigoField    = new JTextField();
                JTextField vencimentoField = new JTextField();
                int r = JOptionPane.showConfirmDialog(null,
                        new Object[]{"Código de Barras:", codigoField, "Vencimento (dd/MM/yyyy):", vencimentoField},
                        "Boleto", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    if (codigoField.getText().isEmpty() || vencimentoField.getText().isEmpty())
                        throw new IllegalArgumentException("Preencha todos os campos do boleto.");
                    pagamento = new Boleto(0, codigoField.getText(), vencimentoField.getText());
                }
            } else if (cartaoRB.isSelected()) {
                JTextField nomeField    = new JTextField();
                JTextField numField     = new JTextField();
                JTextField cvvField     = new JTextField();
                JTextField validadeField = new JTextField();
                int r = JOptionPane.showConfirmDialog(null,
                        new Object[]{"Titular:", nomeField, "Número:", numField, "CVV:", cvvField, "Validade:", validadeField},
                        "Cartão", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    if (nomeField.getText().isEmpty() || numField.getText().isEmpty()
                            || cvvField.getText().isEmpty() || validadeField.getText().isEmpty())
                        throw new IllegalArgumentException("Preencha todos os campos do cartão.");
                    pagamento = new Cartao(0, nomeField.getText(), numField.getText(),
                            Integer.parseInt(cvvField.getText()), validadeField.getText());
                }
            } else if (pixRB.isSelected()) {
                JTextField chaveField = new JTextField();
                int r = JOptionPane.showConfirmDialog(null,
                        new Object[]{"Chave Pix:", chaveField}, "Pix", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    if (chaveField.getText().isEmpty())
                        throw new IllegalArgumentException("A chave Pix deve ser preenchida.");
                    pagamento = new Pix(0, chaveField.getText());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma forma de pagamento!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
 
            if (pagamento != null && pagamento.validarInformacoes()) {
                pagamento.aplicarDesconto(carrinho);
                JOptionPane.showMessageDialog(null,
                        "Desconto aplicado. Total: R$ " + String.format("%.2f", carrinho.getTotalCompra()));
                carrinho.comprar();
                JOptionPane.showMessageDialog(null, "Compra realizada com sucesso!");
                dispose();
                TelaInicialCliente nova = new TelaInicialCliente();
                nova.setUsuario(this.usuario);
                nova.setVisible(true);
            } else if (pagamento != null) {
                JOptionPane.showMessageDialog(null,
                        "Informações de pagamento inválidas. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
 
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
 

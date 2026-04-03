package view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import java.util.List;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import model.beans.Album;
import model.beans.Filme;
import model.beans.Jogo;
import model.beans.Midia;
import model.dao.AlbumDAO;
import model.dao.FilmeDAO;
import model.dao.JogoDAO;
import model.dao.ListagemDAO;
import model.dao.MidiaDAO;


public class Home extends JFrame {
	
	AlbumDAO albumDAO = new AlbumDAO();
	FilmeDAO filmeDAO = new FilmeDAO();
	JogoDAO jogoDAO = new JogoDAO();

	
    private JRadioButton filmeRadioButton;
    private JRadioButton musicaRadioButton;
    private JRadioButton jogoRadioButton;


    public Home() {
    	MidiaDAO midiaDAO = new MidiaDAO();
    	Midia ultimaMidiaAdicionada = midiaDAO.obterUltimaMidia();
    	
        setTitle("Meu Aplicativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 900);

        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        JPanel headerPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);

     // ── Barra de busca com botão ..... ──────────────────────────────────────────
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

        JToolBar toolBar = new JToolBar();
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        
        JButton cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exibirOpcoesDeCadastro();
            }
        });
        toolBar.add(cadastrarButton);

        JButton btn2 = new JButton("Listar Mídias");
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               exibirOpcoesDeListagem();
            }
        });
        toolBar.add(btn2);

        JButton btn3 = new JButton("Alterar Mídias");
        btn3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               exibirOpcoesDeAlteracao();
            }
        });
        toolBar.add(btn3);
        
        JButton btn4 = new JButton("Deletar Mídias");
        btn4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               exibirOpcoesDeExclusao();
            }
        });
        toolBar.add(btn4);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login();
                login.setVisible(true);
            }
        });
        toolBar.add(btnSair);
        
        headerPanel.add(toolBar, BorderLayout.SOUTH);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(128, 128, 192));
        headerPanel.add(panel, BorderLayout.CENTER);
        
        JLabel titulo = new JLabel("GALAXY");
        titulo.setForeground(new Color(255, 255, 255));
        panel.add(titulo);
        
        JPanel footerPanel = new JPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        JLabel footerLabel = new JLabel("GALAXY© TODOS OS DIREITOS RESERVADOS");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerPanel.add(footerLabel);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        java.util.List<Midia> todasMidias = midiaDAO.listarTodasMidias();

        for (Midia m : todasMidias) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                BorderFactory.createLineBorder(new Color(200, 200, 220))
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

            JLabel imgLabel = new JLabel();
            imgLabel.setPreferredSize(new Dimension(110, 140));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                URL url = new URL(m.getCapa());
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(110, 140, Image.SCALE_SMOOTH);
                imgLabel.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                imgLabel.setText("Sem foto");
            }
            card.add(imgLabel, BorderLayout.WEST);

            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            info.add(new JLabel("ID: " + m.getIdMidia()));
            info.add(new JLabel("Nome: " + m.getNome()));
            info.add(new JLabel("Tipo: " + m.getTipo()));
            info.add(new JLabel("Preço: R$ " + String.format("%.2f", m.getPreco())));
            info.add(new JLabel("Classificação: " + m.getClassIndicativa()));
            info.add(new JLabel("Gênero: " + m.getGenero()));
            info.add(new JLabel("Estoque: " + m.getQtdEstoque()));
            card.add(info, BorderLayout.CENTER);

            centerPanel.add(card);
        }

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
                    JOptionPane.showMessageDialog(Home.this, "Digite um nome para pesquisar.");
                }
            }
        };
        searchField.addActionListener(buscaAction);  // Enter ainda funciona
        searchButton.addActionListener(buscaAction); // Botão também funciona
    }
    

    // ========================================
    // CADASTRO
    // ========================================
    private void exibirOpcoesDeCadastro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ButtonGroup group = new ButtonGroup();

        filmeRadioButton = new JRadioButton("Filmeeeee");
        panel.add(filmeRadioButton, BorderLayout.NORTH);
        group.add(filmeRadioButton);

        musicaRadioButton = new JRadioButton("Album");
        panel.add(musicaRadioButton, BorderLayout.CENTER);
        group.add(musicaRadioButton);

        jogoRadioButton = new JRadioButton("Jogossss");
        panel.add(jogoRadioButton, BorderLayout.SOUTH);
        group.add(jogoRadioButton);

        int result = JOptionPane.showConfirmDialog(this, panel, "Escolha o tipo de mídia", 
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            if (filmeRadioButton.isSelected()) {
                CadastroFilmeFrame cadastroFrame = new CadastroFilmeFrame();
                // ✅ Atualiza a Home após cadastrar
                cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        dispose();
                        new Home().setVisible(true);
                    }
                });
                cadastroFrame.setVisible(true);
                
            } else if (musicaRadioButton.isSelected()) {
                CadastroAlbumFrame cadastroFrame = new CadastroAlbumFrame();
                // ✅ Atualiza a Home após cadastrar
                cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        dispose();
                        new Home().setVisible(true);
                    }
                });
                cadastroFrame.setVisible(true);
                
            } else if (jogoRadioButton.isSelected()) {
                CadastroJogoFrame cadastroFrame = new CadastroJogoFrame();
                // ✅ Atualiza a Home após cadastrar
                cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        dispose();
                        new Home().setVisible(true);
                    }
                });
                cadastroFrame.setVisible(true);
            
            } else {
                JOptionPane.showMessageDialog(null, "Selecione uma opção!");
            }
        }
    }
    
    // ========================================
    // LISTAGEM
    // ========================================
    private void exibirOpcoesDeListagem() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ButtonGroup group = new ButtonGroup();

        filmeRadioButton = new JRadioButton("Filme");
        panel.add(filmeRadioButton, BorderLayout.NORTH);
        group.add(filmeRadioButton);

        musicaRadioButton = new JRadioButton("Album");
        panel.add(musicaRadioButton, BorderLayout.CENTER);
        group.add(musicaRadioButton);

        jogoRadioButton = new JRadioButton("Jogo");
        panel.add(jogoRadioButton, BorderLayout.SOUTH);
        group.add(jogoRadioButton);

        JOptionPane.showMessageDialog(this, panel, "Escolha o tipo de mídia", JOptionPane.PLAIN_MESSAGE);

        if (filmeRadioButton.isSelected()) {
            ListagemFrame<Filme> listagemFrame = new ListagemFrame<>("Listagem de Filmes", filmeDAO);
            listagemFrame.setVisible(true);        
        } else if (musicaRadioButton.isSelected()) {
            ListagemFrame<Album> listagemFrame = new ListagemFrame<>("Listagem de Álbuns", albumDAO);
            listagemFrame.setVisible(true); 
        } else if (jogoRadioButton.isSelected()) {
            ListagemFrame<Jogo> listagemFrame = new ListagemFrame<>("Listagem de Jogos", jogoDAO);
            listagemFrame.setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma opção!");
            return;
        }    
    }
    
    // ========================================
    // ✅ ALTERAÇÃO — com WindowListener em todos os três
    // ========================================
    private void exibirOpcoesDeAlteracao() {
    	JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ButtonGroup group = new ButtonGroup();

        filmeRadioButton = new JRadioButton("Filme");
        panel.add(filmeRadioButton, BorderLayout.NORTH);
        group.add(filmeRadioButton);

        musicaRadioButton = new JRadioButton("Album");
        panel.add(musicaRadioButton, BorderLayout.CENTER);
        group.add(musicaRadioButton);

        jogoRadioButton = new JRadioButton("Jogo");
        panel.add(jogoRadioButton, BorderLayout.SOUTH);
        group.add(jogoRadioButton);

        int result = JOptionPane.showConfirmDialog(this, panel, "Escolha o tipo de mídia", 
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                if (filmeRadioButton.isSelected()) {
                    String idStr = JOptionPane.showInputDialog(this, "Informe o ID do filme:");
                    if (idStr == null || idStr.trim().isEmpty()) return;
                    
                    int id = Integer.parseInt(idStr);
                    Filme filme = filmeDAO.buscarFilmePorId(id);
                    
                    if (filme != null) {
                        CadastroFilmeFrame cadastroFrame = new CadastroFilmeFrame(filme);
                        // ✅ Atualiza a Home após salvar
                        cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                dispose();
                                new Home().setVisible(true);
                            }
                        });
                        cadastroFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Filme não encontrado!");
                    }
                    
                } else if (musicaRadioButton.isSelected()) {
                    String idStr = JOptionPane.showInputDialog(this, "Informe o ID do álbum:");
                    if (idStr == null || idStr.trim().isEmpty()) return;
                    
                    int id = Integer.parseInt(idStr);
                    Album album = albumDAO.buscarAlbumPorId(id);
                    
                    if (album != null) {
                        CadastroAlbumFrame cadastroFrame = new CadastroAlbumFrame(album);
                        // ✅ Atualiza a Home após salvar
                        cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                dispose();
                                new Home().setVisible(true);
                            }
                        });
                        cadastroFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Álbum não encontrado!");
                    }
                    
                } else if (jogoRadioButton.isSelected()) {
                    String idStr = JOptionPane.showInputDialog(this, "Informe o ID do jogo:");
                    if (idStr == null || idStr.trim().isEmpty()) return;
                    
                    int id = Integer.parseInt(idStr);
                    Jogo jogo = jogoDAO.buscarJogoPorId(id);
                    
                    if (jogo != null) {
                        CadastroJogoFrame cadastroFrame = new CadastroJogoFrame(jogo);
                        // ✅ Atualiza a Home após salvar
                        cadastroFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                dispose();
                                new Home().setVisible(true);
                            }
                        });
                        cadastroFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Jogo não encontrado!");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma opção!");
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido! Digite apenas números.", 
                                            "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ========================================
    // EXCLUSÃO (já funcionava, mantido igual)
    // ========================================
    private void exibirOpcoesDeExclusao() {
    	JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        ButtonGroup group = new ButtonGroup();

        filmeRadioButton = new JRadioButton("Filme");
        panel.add(filmeRadioButton, BorderLayout.NORTH);
        group.add(filmeRadioButton);

        musicaRadioButton = new JRadioButton("Album");
        panel.add(musicaRadioButton, BorderLayout.CENTER);
        group.add(musicaRadioButton);

        jogoRadioButton = new JRadioButton("Jogo");
        panel.add(jogoRadioButton, BorderLayout.SOUTH);
        group.add(jogoRadioButton);

        JOptionPane.showMessageDialog(this, panel, "Escolha o tipo de mídia", JOptionPane.PLAIN_MESSAGE);

        if (filmeRadioButton.isSelected()) {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Opção selecionada: Filme\n\nInforme o ID do filme:"));
            filmeDAO.deletarFilme(id);
            dispose();
            new Home().setVisible(true);
            
        } else if (musicaRadioButton.isSelected()) {
        	int id = Integer.parseInt(JOptionPane.showInputDialog("Opção selecionada: Album\n\nInforme o ID do album:"));
            albumDAO.deletarAlbum(id);
            dispose();
            new Home().setVisible(true);
            
        } else if (jogoRadioButton.isSelected()) {
        	int id = Integer.parseInt(JOptionPane.showInputDialog("Opção selecionada: Jogo\n\nInforme o ID do jogo:"));
            jogoDAO.deletarJogo(id);
            dispose();
            new Home().setVisible(true);
        
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma opção!");
        }
    }
}
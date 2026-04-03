package model.dao;

import model.beans.Filme;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.JPanel;

public class FilmeDAO implements ListagemDAO<Filme>{
	
	PreparedStatement st;
	ResultSet rs;

	// NOVO MÉTODO - Aceita objeto Filme ao invés de usar JOptionPane
	public void cadastrarFilmeObjeto(Filme filme) {
		try (Connection conecta = ConectaBD.conexao()) {
			String elencoString = String.join(",", filme.getElenco());

			try {
				conecta.setAutoCommit(false);
				
				String sqlMidia = "INSERT INTO midia (nome, preco, classIndicativa, genero, qtdEstoque, capa, tipo) VALUES (?,?,?,?,?,?,?)";
				st = conecta.prepareStatement(sqlMidia, PreparedStatement.RETURN_GENERATED_KEYS);
				st.setString(1, filme.getNome());
				st.setDouble(2, filme.getPreco());
				st.setString(3, filme.getClassIndicativa());
				st.setString(4, filme.getGenero());
				st.setInt(5, filme.getQtdEstoque());
				st.setString(6, filme.getCapa());
				st.setString(7, "filme");
				st.executeUpdate();
				
				rs = st.getGeneratedKeys();
				int lastId = 0;
				if (rs.next()) {
					lastId = rs.getInt(1);
				}	
				
				rs.close();
				st.close();
				
				String sqlFilme = "INSERT INTO filme (idFilme, diretor, duracao, elenco) VALUES (?,?,?,?)";
				st = conecta.prepareStatement(sqlFilme);
				st.setInt(1, lastId);
				st.setString(2, filme.getDiretor());
				st.setString(3, filme.getDuracao());
				st.setString(4, elencoString);
				
				st.executeUpdate();
				conecta.commit();
				
				JOptionPane.showMessageDialog(null, "Filme cadastrado com sucesso!");
			} catch (SQLException e) {
				conecta.rollback();
				String erro = e.getMessage();
				if (erro.contains("Duplicate entry")) {
					JOptionPane.showMessageDialog(null, "Filme já cadastrado!");
				} else {
					JOptionPane.showMessageDialog(null, "Erro ao cadastrar filme: " + erro);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
		}
	}
	
	// NOVO MÉTODO - Aceita objeto Filme para atualização
	public void alterarFilmeObjeto(int idFilme, Filme filme) {
		try (Connection conecta = ConectaBD.conexao()) {
			String elencoString = String.join(",", filme.getElenco());

			try {
				st = conecta.prepareStatement(
					"UPDATE midia SET nome = ?, preco = ?, classIndicativa = ?, genero = ?, qtdEstoque = ?, capa = ? WHERE idMidia = ?");
				st.setString(1, filme.getNome());
				st.setDouble(2, filme.getPreco());
				st.setString(3, filme.getClassIndicativa());
				st.setString(4, filme.getGenero());
				st.setInt(5, filme.getQtdEstoque());
				st.setString(6, filme.getCapa());
				st.setInt(7, idFilme);
				st.executeUpdate();
				st.close();

				st = conecta.prepareStatement(
					"UPDATE filme SET diretor = ?, duracao = ?, elenco = ? WHERE idFilme = ?");
				st.setString(1, filme.getDiretor());
				st.setString(2, filme.getDuracao());
				st.setString(3, elencoString);
				st.setInt(4, idFilme);
				st.executeUpdate();
				st.close();

				JOptionPane.showMessageDialog(null, "Filme atualizado com sucesso!");
			} catch (SQLException e) {
				String erro = e.getMessage();
				if (erro.contains("Duplicate entry")) {
					JOptionPane.showMessageDialog(null, "Filme já cadastrado!");
				} else {
					JOptionPane.showMessageDialog(null, "Erro ao atualizar filme: " + erro);
				}
			}

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
		}
	}

	// MANTIDO - Método antigo com JOptionPane (para compatibilidade)
	public void cadastrarFilme() {
		try (Connection conecta = ConectaBD.conexao()) {
			Filme filme = new Filme();
			filme.setNome(JOptionPane.showInputDialog("Digite o nome do filme: "));
			filme.setPreco(Double.parseDouble(JOptionPane.showInputDialog("Digite o preço do filme: ")));
			filme.setClassIndicativa(JOptionPane.showInputDialog("Digite a classificação indicativa do filme: "));
			filme.setGenero(JOptionPane.showInputDialog("Digite o gênero do filme: "));
			filme.setQtdEstoque(Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade em estoque do filme: ")));
			filme.setCapa(JOptionPane.showInputDialog("Informe a URL da capa do filme: "));
			
			filme.setDiretor(JOptionPane.showInputDialog("Informe o nome do diretor do filme"));
			filme.setDuracao(JOptionPane.showInputDialog("Informe a duração do filme: "));
			
			int qtdElenco = Integer.parseInt(JOptionPane.showInputDialog("Informe a quantidade de atores no elenco: "));
			for(int i = 0; i < qtdElenco; i++) {
				filme.adicionarAtor(JOptionPane.showInputDialog("Informe o nome do ator"));
			}
			
			// Chama o novo método
			cadastrarFilmeObjeto(filme);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
		}
	}
	
	@Override
	public void listar(JPanel mainPanel) {
        try (Connection conecta = ConectaBD.conexao()) {
            String sql = "SELECT m.idMidia, m.nome, m.preco, m.classIndicativa, m.genero, m.qtdEstoque, m.capa, f.diretor, f.duracao, f.elenco, m.tipo "
                       + "FROM midia m "
                       + "INNER JOIN filme f ON m.idMidia = f.idFilme";
            
            try (PreparedStatement st = conecta.prepareStatement(sql);
                 ResultSet rs = st.executeQuery()) {
                
                while (rs.next()) {
                    JPanel midiaPanel = new JPanel();
                    midiaPanel.setLayout(new BorderLayout());
                    midiaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JPanel infoPanel = new JPanel();
                    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                    midiaPanel.add(infoPanel, BorderLayout.CENTER);

						                    String capaUrl = rs.getString("capa");
						                    try {
						                        URL url = new URL(capaUrl);
						                        ImageIcon icon = new ImageIcon(url);
						                        JLabel imageLabel = new JLabel(icon);
						                        midiaPanel.add(imageLabel, BorderLayout.WEST);
						                    } catch (Exception e) {
						                        e.printStackTrace();
						                    }

                    infoPanel.add(new JLabel("ID: " + rs.getInt("idMidia")));
                    infoPanel.add(new JLabel("Nome: " + rs.getString("nome")));
                    infoPanel.add(new JLabel("Preço: " + String.format("%.2f",rs.getDouble("preco"))));
                    infoPanel.add(new JLabel("Classificação Indicativa: " + rs.getString("classIndicativa")));
                    infoPanel.add(new JLabel("Gênero: " + rs.getString("genero")));
                    infoPanel.add(new JLabel("Quantidade em Estoque: " + rs.getInt("qtdEstoque")));
                    infoPanel.add(new JLabel("Diretor: " + rs.getString("diretor")));
                    infoPanel.add(new JLabel("Duração: " + rs.getString("duracao")));
                    infoPanel.add(new JLabel("Elenco: " + rs.getString("elenco")));
                    infoPanel.add(new JLabel("Tipo: " + rs.getString("tipo")));

                    mainPanel.add(midiaPanel);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao listar filmes: " + e.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

	public void consultarFilme(String nome) {
		String dadosFilmes = "";
        try (Connection conecta = ConectaBD.conexao()) {
            String sql = "SELECT m.idMidia, m.nome, m.preco, m.classIndicativa, m.genero, m.qtdEstoque, m.capa, f.diretor, f.duracao, f.elenco, m.tipo "
                       + "FROM midia m "
                       + "INNER JOIN filme f ON m.idMidia = f.idFilme "
                       + "WHERE m.nome LIKE ? "
                       + "ORDER BY m.idMidia";
            
            try (PreparedStatement st = conecta.prepareStatement(sql)) {
                st.setString(1, "%" + nome + "%");
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        dadosFilmes += "ID: " + rs.getInt("idMidia") + "\n" 
                                     + "Nome: " + rs.getString("nome") + "\n"
                                     + "Preço: " + rs.getDouble("preco") + "\n"
                                     + "Classificação Indicativa: " + rs.getString("classIndicativa") + "\n"
                                     + "Gênero: " + rs.getString("genero") + "\n"
                                     + "Quantidade em Estoque: " + rs.getInt("qtdEstoque") + "\n"
                                     + "Capa: " + rs.getString("capa") + "\n"
                                     + "Diretor: " + rs.getString("diretor") + "\n"
                                     + "Duração: " + rs.getString("duracao") + "\n"
                                     + "Elenco: " + rs.getString("elenco") + "\n"
                                     + "Tipo: " + rs.getString("tipo") + "\n\n";
                    }
                    JOptionPane.showMessageDialog(null, dadosFilmes);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar filme: " + e.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

	// MANTIDO - Método antigo com JOptionPane
	public void alterarFilme(int idFilme) {
		try (Connection conecta = ConectaBD.conexao()) {
            Filme filme = new Filme();
            filme.setNome(JOptionPane.showInputDialog("Digite o nome do filme: "));
            filme.setPreco(Double.parseDouble(JOptionPane.showInputDialog("Digite o preço do filme: ")));
            filme.setClassIndicativa(JOptionPane.showInputDialog("Digite a classificação indicativa do filme: "));
            filme.setGenero(JOptionPane.showInputDialog("Digite o gênero do filme: "));
            filme.setQtdEstoque(Integer.parseInt(JOptionPane.showInputDialog("Digite a quantidade em estoque do filme: ")));
            filme.setCapa(JOptionPane.showInputDialog("Informe a URL da capa do filme: "));
            filme.setDiretor(JOptionPane.showInputDialog("Informe o nome do diretor do filme"));
            filme.setDuracao(JOptionPane.showInputDialog("Informe a duração do filme: "));

            int qtdElenco = Integer.parseInt(JOptionPane.showInputDialog("Informe a quantidade de atores no elenco: "));
            for (int i = 0; i < qtdElenco; i++) {
                filme.adicionarAtor(JOptionPane.showInputDialog("Informe o nome do ator"));
            }

            // Chama o novo método
            alterarFilmeObjeto(idFilme, filme);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }

	public void deletarFilme(int idFilme) {
		try (Connection conecta = ConectaBD.conexao()) {
            try {
                st = conecta.prepareStatement("DELETE FROM filme WHERE idFilme=?");
                st.setInt(1, idFilme);
                int resultadoFilme = st.executeUpdate();
                st.close();

                if (resultadoFilme == 0) {
                    JOptionPane.showMessageDialog(null, "Filme não encontrado");
                    return;
                }

                st = conecta.prepareStatement("DELETE FROM midia WHERE idMidia=?");
                st.setInt(1, idFilme);
                int resultadoMidia = st.executeUpdate();
                st.close();

                if (resultadoMidia == 0)
                    JOptionPane.showMessageDialog(null, "Mídia não encontrada");
                else
                    JOptionPane.showMessageDialog(null, "O filme e a mídia de registro " + idFilme + " foram removidos com sucesso");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Erro ao deletar filme: " + e.getMessage());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
	
	public Filme obterUltimoFilmeAdicionado() {
	    Filme ultimoFilme = null;

	    try (Connection conecta = ConectaBD.conexao()) {
	    	try {
	    		st = conecta.prepareStatement("SELECT * FROM filme ORDER BY idFilme DESC LIMIT 1");
	    		ResultSet rs = st.executeQuery();

	        if (rs.next()) {
	        	int idMidia = rs.getInt("idMidia");
	        	int id = rs.getInt("idFilme");
	            String nome = rs.getString("nome");
	            double preco = rs.getFloat("preco");
	            String classIndicativa = rs.getString("classIndicativa");
	            String genero = rs.getString("genero");
	            int qtdEstoque = rs.getInt("qtdEstoque");
	            String capa = rs.getString("capa");
	            String diretor = rs.getString("diretor");
	            String duracao = rs.getString("duracao");
	            String elencoBD = rs.getString("elenco");
	            String tipo = rs.getString("tipo");
	            
	            ArrayList<String> elenco = new ArrayList<>();
	            if (elencoBD != null && !elencoBD.isEmpty()) {
	                String[] atores = elencoBD.split(",");
	                elenco.addAll(Arrays.asList(atores));
	            }
	            
	            ultimoFilme = new Filme(idMidia, nome, preco, classIndicativa, genero, qtdEstoque, capa, tipo, id, diretor, duracao, elenco);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    } catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados: " + e.getMessage());

	}
	    return ultimoFilme;
	}
	
	// MÉTODO PARA BUSCAR FILME POR ID (NECESSÁRIO PARA EDIÇÃO)
	public Filme buscarFilmePorId(int id) {
	    Filme filme = null;
	    
	    try (Connection conecta = ConectaBD.conexao()) {
	        String sql = "SELECT m.idMidia, m.nome, m.preco, m.classIndicativa, m.genero, m.qtdEstoque, m.capa, m.tipo, " +
	                     "f.idFilme, f.diretor, f.duracao, f.elenco " +
	                     "FROM midia m " +
	                     "INNER JOIN filme f ON m.idMidia = f.idFilme " +
	                     "WHERE m.idMidia = ?";
	        
	        PreparedStatement st = conecta.prepareStatement(sql);
	        st.setInt(1, id);
	        ResultSet rs = st.executeQuery();
	        
	        if (rs.next()) {
	            int idMidia = rs.getInt("idMidia");
	            String nome = rs.getString("nome");
	            double preco = rs.getDouble("preco");
	            String classIndicativa = rs.getString("classIndicativa");
	            String genero = rs.getString("genero");
	            int qtdEstoque = rs.getInt("qtdEstoque");
	            String capa = rs.getString("capa");
	         // Dentro do seu método de listagem, onde você processa o ResultSet (rs)
	            JLabel labelCapa = new JLabel();

	            try {
	                if (capa != null && !capa.isEmpty()) {
	                    URL url = new URL(capa);
	                    ImageIcon originalIcon = new ImageIcon(url);
	                    
	                    // --- AQUI ESTÁ O TRATAMENTO ---
	                    // Definimos um tamanho padrão (ex: 150 largura x 200 altura)
	                    Image image = originalIcon.getImage();
	                    Image resizedImage = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
	                    
	                    labelCapa.setIcon(new ImageIcon(resizedImage));
	                } else {
	                    labelCapa.setText("Sem Capa");
	                }
	            } catch (Exception e) {
	                labelCapa.setText("Erro na imagem");
	            }

	            // Adiciona o labelCapa ao painel do item...
	            String tipo = rs.getString("tipo");
	            
	            int idFilme = rs.getInt("idFilme");
	            String diretor = rs.getString("diretor");
	            String duracao = rs.getString("duracao");
	            String elencoBD = rs.getString("elenco");
	            
	            ArrayList<String> elenco = new ArrayList<>();
	            if (elencoBD != null && !elencoBD.isEmpty()) {
	                String[] atores = elencoBD.split(",");
	                for (String ator : atores) {
	                    elenco.add(ator.trim());
	                }
	            }
	            
	            filme = new Filme(idMidia, nome, preco, classIndicativa, genero, qtdEstoque, 
	                            capa, tipo, idFilme, diretor, duracao, elenco);
	        }
	        
	        rs.close();
	        st.close();
	        
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Erro ao buscar filme: " + e.getMessage());
	    }
	    
	    return filme;
	}
}
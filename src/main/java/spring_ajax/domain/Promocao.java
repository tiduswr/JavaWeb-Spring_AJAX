package spring_ajax.domain;

import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promocoes")
public class Promocao implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Um titulo é necessário!")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotBlank(message = "Um link da promoção é necessário!")
    @Column(name = "link_promocao", nullable = false)
    private String linkPromocao;

    @Column(name = "site_promocao", nullable = false)
    private String site;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "link_imagem", nullable = false)
    private String linkImagem;

    @NotNull(message = "Um preço é necessário!")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(name = "preco_promocao", nullable = false)
    private BigDecimal preco;

    @Column(name = "total_likes")
    private int likes;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dtCadastro;

    @NotNull(message = "Uma categoria é necessário!")
    @ManyToOne
    @JoinColumn(name = "categoria_fk")
    private Categoria categoria;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLinkPromocao() {
        return linkPromocao;
    }

    public void setLinkPromocao(String linkPromocao) {
        this.linkPromocao = linkPromocao;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLinkImagem() {
        return linkImagem;
    }

    public void setLinkImagem(String linkImagem) {
        this.linkImagem = linkImagem;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public LocalDate getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(LocalDate dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    @Override
    public String toString() {
        return "Promocao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", linkPromocao='" + linkPromocao + '\'' +
                ", site='" + site + '\'' +
                ", descricao='" + descricao + '\'' +
                ", linkImagem='" + linkImagem + '\'' +
                ", preco=" + preco +
                ", likes=" + likes +
                ", dtCadastro=" + dtCadastro +
                ", categoria=" + categoria +
                '}';
    }
}

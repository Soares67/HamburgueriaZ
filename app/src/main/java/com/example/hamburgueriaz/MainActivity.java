package com.example.hamburgueriaz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Variáveis globais
    private int quantidade = 0;

    // Componentes da interface
    private EditText etNomeCliente;
    private CheckBox cbBacon, cbQueijo, cbOnion;
    private TextView tvQuantidade, tvTotal, tvResumo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ligação dos componentes
        etNomeCliente = findViewById(R.id.etNomeCliente);
        cbBacon = findViewById(R.id.cbBacon);
        cbQueijo = findViewById(R.id.cbQueijo);
        cbOnion = findViewById(R.id.cbOnion);
        tvQuantidade = findViewById(R.id.tvQuantidade);
        tvTotal = findViewById(R.id.tvTotal);
        tvResumo = findViewById(R.id.tvResumo);
        Button btnMais = findViewById(R.id.btnMais);
        Button btnMenos = findViewById(R.id.btnMenos);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        // Clique no botão +
        btnMais.setOnClickListener(v -> {
            quantidade++;
            atualizarQuantidade();
        });

        // Clique no botão -
        btnMenos.setOnClickListener(v -> {
            if (quantidade > 0) {
                quantidade--;
                atualizarQuantidade();
            }
        });

        // Adicionando listeners aos checkboxes
        cbBacon.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarTotal());
        cbQueijo.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarTotal());
        cbOnion.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarTotal());

        // Clique no botão Enviar Pedido
        btnEnviar.setOnClickListener(v -> enviarPedido());
    }

    // Atualiza o TextView com a quantidade e total
    private void atualizarQuantidade() {
        tvQuantidade.setText(String.valueOf(quantidade));
        atualizarTotal();
    }

    // Calcula o total com base nos adicionais e quantidade
    private void atualizarTotal() {
        int total = calcularPrecoTotal();
        tvTotal.setText("Total: R$ " + total + ",00");
    }

    // Calcula o valor total do pedido
    private int calcularPrecoTotal() {
        int adicionais = 0;
        int PRECO_BACON = 2;
        if (cbBacon.isChecked()) adicionais += PRECO_BACON;
        int PRECO_QUEIJO = 2;
        if (cbQueijo.isChecked()) adicionais += PRECO_QUEIJO;
        int PRECO_ONION = 3;
        if (cbOnion.isChecked()) adicionais += PRECO_ONION;

        int PRECO_BASE = 20;
        return quantidade * (PRECO_BASE + adicionais);
    }

    // Gera resumo e dispara Intent de e-mail
    @SuppressLint("QueryPermissionsNeeded")
    private void enviarPedido() {
        String nome = etNomeCliente.getText().toString().trim();
        if (nome.isEmpty()) {
            Toast.makeText(this, "Digite o nome do cliente.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean temBacon = cbBacon.isChecked();
        boolean temQueijo = cbQueijo.isChecked();
        boolean temOnion = cbOnion.isChecked();

        int total = calcularPrecoTotal();

        String resumo = "Nome do cliente: " + nome + "\n" +
                "Tem Bacon? " + (temBacon ? "Sim" : "Não") + "\n" +
                "Tem Queijo? " + (temQueijo ? "Sim" : "Não") + "\n" +
                "Tem Onion Rings? " + (temOnion ? "Sim" : "Não") + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Preço final: R$ " + total + ",00";

        tvResumo.setText(resumo);
        tvTotal.setText("Total: R$ " + total + ",00");

        // Intent de envio de e-mail
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"exemplo@email.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome );
        emailIntent.putExtra(Intent.EXTRA_TEXT, resumo);

        try {
            startActivity(Intent.createChooser(emailIntent, "Escolha um app de e-mail"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_SHORT).show();
        }}}
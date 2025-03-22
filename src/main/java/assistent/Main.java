package assistent;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.*;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {

    /**
     * Constantes utilizadas para configurar o chat.
     */
    private static final int TOKEN_THRESHOLD = 1000;
    private static final int MAX_COMPLETION_TOKENS = 500;
    private static final double TEMPERATURE = 0.2;

    /**
     * Método principal que inicia o programa.
     */
    public static void main(String[] args) {
        SimpleOpenAI openAI = initializeOpenAI();
        String systemContext = createSystemContext();
        List<String> warehouseData = loadWarehouseData();

        if (warehouseData == null) return;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Digite sua pergunta (ou 'sair' para finalizar): ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando o chat.");
                    break;
                }

                String model = determineModel();
                String response = getChatResponse(openAI, systemContext, userInput, warehouseData, model);
                System.out.println(response);
            }
        }
    }

    /**
     * Inicializa a instância do SimpleOpenAI.
     *
     * @return Instância inicializada do SimpleOpenAI.
     */
    private static SimpleOpenAI initializeOpenAI() {
        return SimpleOpenAI.builder()
                .apiKey(System.getenv("API_KEY")) // API_KEY é uma variável de ambiente que contém a chave da API OpenAI.
                .build();
    }

    /**
     * Cria o contexto do sistema para o assistente.
     *
     * @return String contendo o contexto do sistema.
     */
    private static String createSystemContext() {
        return "Você é um assistente especializado em almoxarifado. " +
                "Sua função é fornecer suporte exclusivo relacionado à gestão de estoque, controle de materiais, entrada e saída de itens, localização de produtos, validade, lote e outras operações típicas de almoxarifado. " +
                "Não responda perguntas fora desse contexto. " +
                "Sempre responda com os dados solicitados, sem explicar como foram obtidos. " +
                "O retorno deve ser formatado conforme sua coluna contendo apenas nome e posição, ex: Nome: Hélice - Posição Estoque: 286. " +
                "Se a solicitação não estiver relacionada a almoxarifado, informe educadamente que você só pode ajudar com assuntos desse domínio. " +
                "Evite fornecer opiniões, conselhos pessoais, ou tratar de temas técnicos fora do escopo de almoxarifado, como TI, finanças, jurídico ou RH.";
    }

    /**
     * Carrega os dados do almoxarifado a partir de um arquivo.
     *
     * @return Lista de strings contendo os dados do almoxarifado.
     */
    private static List<String> loadWarehouseData() {
        try {
            return Files.readAllLines(Paths.get("src/main/resources/dados.txt"));
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo dados.txt: " + e.getMessage());
            return null;
        }
    }

    /**
     * Determina o modelo a ser utilizado com base na quantidade de tokens.
     *
     * @return String contendo o nome do modelo a ser utilizado.
     */
    private static String determineModel() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding encoding = registry.getEncodingForModel(ModelType.GPT_4O);
        int tokenCount = encoding.countTokens("This is a sample sentence.");
        System.out.println("Quantidade de tokens: " + tokenCount);
        return tokenCount < TOKEN_THRESHOLD ? "gpt-3" : "gpt-4o";
    }

    /**
     * Gera uma resposta de chat a partir da API OpenAI com base nos parâmetros fornecidos.
     *
     * @param openAI Instância inicializada do SimpleOpenAI.
     * @param systemContext String contendo o contexto do sistema.
     * @param userInput Entrada do usuário.
     * @param warehouseData Lista de dados do almoxarifado.
     * @param model Modelo a ser utilizado para gerar a resposta do chat.
     * @return Conteúdo da primeira mensagem na resposta do chat.
     */
    private static String getChatResponse(SimpleOpenAI openAI, String systemContext, String userInput, List<String> warehouseData, String model) {
        ChatRequest chatRequest = ChatRequest.builder()
                .model(model)
                .message(ChatMessage.SystemMessage.of(systemContext + "\n" + userInput + "\n" + warehouseData))
                .temperature(TEMPERATURE)
                .maxCompletionTokens(MAX_COMPLETION_TOKENS)
                .build();
        Chat chatResponse = openAI.chatCompletions().create(chatRequest).join();
        return chatResponse.firstContent();
    }
}
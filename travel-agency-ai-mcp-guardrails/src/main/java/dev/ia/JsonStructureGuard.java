package dev.ia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import java.io.StringReader;

@ApplicationScoped
public class JsonStructureGuard implements OutputGuardrail {
    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        String response = aiMessage.text();
        try (JsonReader reader = Json.createReader(new StringReader(response))) {
            // Tenta fazer o parse. Se falhar, o JSON é inválido.
            JsonObject jsonObject = reader.readObject();
            return OutputGuardrailResult.success();
        } catch (Exception e) {
            // Ensinamos o modelo como corrigir
            return reprompt(aiMessage.text(), """
            Erro: Sua resposta não é um JSON válido.
            Problema encontrado: """ + e.getMessage() + """
            . Gere NOVAMENTE apenas o JSON, sem blocos de código markdown ou texto adicional.
            """);
        }
    }
}
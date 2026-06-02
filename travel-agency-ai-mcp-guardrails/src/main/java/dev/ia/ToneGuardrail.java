package dev.ia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.data.message.AiMessage;

@ApplicationScoped
public class ToneGuardrail implements OutputGuardrail {

    @Inject
    ToneJudge toneJudge; // OLLAMA/QUARKUS IRÁ INJETAR AQUI

    @Override
    public OutputGuardrailResult validate(AiMessage aiMessage) {
        if (!toneJudge.isProfessional(aiMessage.text())) {
            return reprompt(aiMessage.text(), """
                    Sua resposta foi detectada como rude ou informal demais.
                    Reescreva-a mantendo a polidez e formalidade de um agente de viagens sênior.
                    """);
        }
        return OutputGuardrailResult.success();
    }
}

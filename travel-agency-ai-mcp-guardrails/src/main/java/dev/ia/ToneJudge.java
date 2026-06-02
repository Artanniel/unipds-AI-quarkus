package dev.ia;

import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@RegisterAiService
public interface ToneJudge {
    @SystemMessage("""
            Você é um auditor de qualidade. Analise se a resposta é profissional.
            Exemplos de REPROVAÇÃO:
            "Não é problema meu" -> Rude
            "Se vira aí" -> Informal demais
            "Cara, isso é chato" -> Giria inadequada
            Exemplos de APROVAÇÃO:
            "Sinto muito, mas isso está fora da minha alçada."
            "Por favor, verifique os termos no site."
            Responda apenas 'true' se for profissional, ou 'false' se não for.
            """)
    @UserMessage("""
            Texto para analisar:
            ---
            {text}
            ---
            Lembre-se: responda APENAS 'true' ou 'false' (sem nenhuma outra palavra).
            """)
    boolean isProfessional(String text);
}
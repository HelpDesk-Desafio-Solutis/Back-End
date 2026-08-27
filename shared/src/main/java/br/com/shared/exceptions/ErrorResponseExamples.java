package br.com.shared.exceptions;

public class ErrorResponseExamples {

    public static final String NOT_FOUND = """
            {
              "status": 404,
              "erro": "Recurso não encontrado"
            }
            """;

    public static final String UNAUTHORIZED = """
            {
              "status": 401,
              "erro": "Não autorizado",
              "mensagem": "Token de autenticação inválido ou ausente"
            }
            """;

    public static final String FORBIDDEN = """
            {
              "status": 403,
              "erro": "Acesso proibido",
              "mensagem": "Você não tem permissão para acessar este recurso"
            }
            """;

    public static final String NO_CONTENT = """
            {
              "status": 204,
              "mensagem": "A requisição foi bem-sucedida, mas não há conteúdo para retornar"
            }
            """;

    public static final String BAD_REQUEST = """
            {
              "status": 400,
              "erro": "Requisição inválida",
              "mensagem": "Parâmetros de entrada inválidos"
            }
            """;

    public static final String CREATED = """
            {
              "status": 201,
              "mensagem": "Recurso criado com sucesso"
            }
            """;

    public static final String OK = """
            {
              "status": 200,
              "mensagem": "Requisição bem-sucedida"
            }
            """;

    public static final String CONFLICT = """
            {
              "status": 409,
              "erro": "Conflito",
              "mensagem": "Recurso já existente"
            }
            """;

}

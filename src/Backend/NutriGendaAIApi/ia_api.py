from flask import Flask, request, jsonify
import openai
from flasgger import Swagger

app = Flask(__name__)
swagger = Swagger(app)

api_key = 'sk-proj-naxN8ReYYrwoCEzBGyoJT3BlbkFJeo2yUyX49SHct8ELNg67'
openai.api_key = api_key

@app.route('/chat', methods=['POST'])
def chat():
    """
    Gera sugestões de dieta utilizando o modelo GPT-3.5-turbo.
    ---
    parameters:
      - name: message
        in: body
        type: string
        required: true
        description: Mensagem do usuário para o assistente de dieta
        schema:
          type: object
          required:
            - message
          properties:
            message:
              type: string
    responses:
      200:
        description: Resposta do assistente de dieta
        schema:
          type: object
          properties:
            reply:
              type: string
      500:
        description: Erro interno do servidor
    """
    data = request.get_json()
    user_message = data.get('message', '')
    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "Você é um assistente de dieta."},
                {"role": "user", "content": user_message}
            ],
            max_tokens=500,
            temperature=0.7
        )
        reply = response['choices'][0]['message']['content'].strip()
        return jsonify({"reply": reply})
    except openai.error.OpenAIError as e:
        return jsonify({"error": str(e)}), 500
    except Exception as e:
        return jsonify({"error": "Erro inesperado: " + str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)

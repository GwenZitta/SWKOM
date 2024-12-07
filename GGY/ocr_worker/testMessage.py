import pika
import json


def send_test_message():
    connection = pika.BlockingConnection(pika.ConnectionParameters(host='rabbitmq'))
    channel = connection.channel()

    # Stelle sicher, dass die Queue existiert, bevor wir eine Nachricht senden
    channel.queue_declare(queue='documentQueue', durable=True)

    # Beispielnachricht
    message = {"file_path": "semester-project.pdf"}
    channel.basic_publish(exchange='',
                          routing_key='documentQueue',
                          body=json.dumps(message),
                          properties=pika.BasicProperties(
                              delivery_mode=2,  # Nachricht persistent machen
                          ))

    print("Nachricht gesendet.")
    connection.close()


if __name__ == "__main__":
    send_test_message()

# Elastic

## Chat index mapping

```json
{
  "settings": {
    "analysis": {
      "analyzer": {
        "ik_max_analyzer": {
          "type": "custom",
          "tokenizer": "ik_max_word"
        },
        "ik_smart_analyzer": {
          "type": "custom",
          "tokenizer": "ik_smart"
        }
      }
    }
  },
  "mappings": {
    "properties": {
      "_class": {
        "type": "keyword",
        "index": false,
        "doc_values": false
      },
      "content": {
        "type": "nested",
        "properties": {
          "_class": {
            "type": "keyword",
            "index": false,
            "doc_values": false
          },
          "content": {
            "type": "text",
            "analyzer": "ik_max_word",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "reasoningContent": {
            "type": "text",
            "analyzer": "ik_smart",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          },
          "role": {
            "type": "text",
            "fields": {
              "keyword": {
                "type": "keyword",
                "ignore_above": 256
              }
            }
          }
        }
      },
      "id": {
        "type": "long"
      },
      "model": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "starred": {
        "type": "boolean"
      },
      "topic": {
        "type": "text",
        "analyzer": "ik_smart"
      },
      "userId": {
        "type": "long"
      }
    }
  }
}
```
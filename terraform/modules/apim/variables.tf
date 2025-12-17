# remote states
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
  }
  variable "foodcore-backend-resource-group" {
    type        = string
    description = "Nome do resource group onde o backend está armazenado"
  }

  variable "foodcore-backend-storage-account" {
    type        = string
    description = "Nome da conta de armazenamento onde o backend está armazenado"
  }

  variable "foodcore-backend-container" {
    type        = string
    description = "Nome do contêiner onde o backend está armazenado"
  }

  variable "foodcore-backend-infra-key" {
    type        = string
    description = "Chave do arquivo tfstate do foodcore-infra"
  }

  variable "foodcore-backend-auth-key" {
    type        = string
    description = "Chave do arquivo tfstate do foodcore-auth"
  }

variable "apim_api_name" {
  description = "Nome da API no API Management"
  type        = string
}

variable "apim_api_version" {
  description = "Versão da API no API Management"
  type        = string
}

variable "apim_display_name" {
  description = "Nome exibido da API no API Management"
  type        = string
}

variable "swagger_path" {
  description = "Caminho do arquivo swagger.json"
  type        = string
}

variable "api_ingress_path" {
  type        = string
  description = "Caminho de ingress da API."
}
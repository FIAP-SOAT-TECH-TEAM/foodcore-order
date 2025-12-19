# Commn
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
  }

# remote states
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

# HELM

  variable "release_name" {
    type        = string
    description = "Nome do release do Helm."
    default = "foodcore-order"
  }

  variable "release_namespace" {
    type        = string
    description = "Namespace Kubernetes onde o release será instalado."
    default = "default"
  }

  variable "chart_name" {
    type        = string
    description = "Nome do chart Helm a ser instalado."
  }

  variable "chart_version" {
    type        = string
    description = "Versão do chart Helm."
  }

  variable "docker_image_uri" {
    type        = string
    description = "URI da imagem Docker a ser utilizada."
  }

  variable "docker_image_tag" {
    type        = string
    description = "Tag da imagem Docker a ser utilizada."
  }

  variable "api_ingress_path" {
    type        = string
    description = "Caminho de ingress da API."
  }

# APIM
  variable "apim_api_name" {
    description = "Nome da API no API Management"
    type        = string
    default = "foodcore-order"
  }

  variable "apim_api_version" {
    description = "Versão da API no API Management"
    type        = string
    default     = "1"
  }

  variable "apim_display_name" {
    description = "Nome exibido da API no API Management"
    type        = string
    default     = "Foodcore Order Microsservice"
  }

  variable "swagger_path" {
    description = "Caminho do arquivo swagger.json"
    type        = string
  }
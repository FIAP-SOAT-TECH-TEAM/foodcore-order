module "helm" {
  source = "./modules/helm"

  subscription_id                   = var.subscription_id
  foodcore-backend-container        = var.foodcore-backend-container
  foodcore-backend-infra-key        = var.foodcore-backend-infra-key
  foodcore-backend-resource-group   = var.foodcore-backend-resource-group
  foodcore-backend-storage-account  = var.foodcore-backend-storage-account
  release_name                      = var.release_name
  repository_url                    = local.repository_url
  chart_name                        = var.chart_name
  chart_version                     = var.chart_version
  release_namespace                 = var.release_namespace
  docker_image_uri                  = var.docker_image_uri
  docker_image_tag                  = var.docker_image_tag
  api_ingress_path                  = var.api_ingress_path
}

module "apim" {
  source = "./modules/apim"

  subscription_id                   = var.subscription_id
  foodcore-backend-container        = var.foodcore-backend-container
  foodcore-backend-infra-key        = var.foodcore-backend-infra-key
  foodcore-backend-auth-key         = var.foodcore-backend-auth-key
  foodcore-backend-resource-group   = var.foodcore-backend-resource-group
  foodcore-backend-storage-account  = var.foodcore-backend-storage-account
  apim_api_name                     = var.apim_api_name
  apim_api_version                  = var.apim_api_version
  apim_display_name                 = var.apim_display_name
  swagger_path                      = var.swagger_path
  api_ingress_path                  = local.api_ingress_path_without_slash

  depends_on = [module.helm]
}
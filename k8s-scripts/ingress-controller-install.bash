# Helm을 다운로드하고 압축을 해제합니다.
curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
chmod 700 get_helm.sh
./get_helm.sh

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm install smv ingress-nginx/ingress-nginx \
    --set controller.ingressClassResource.default=true

# helm uninstall smv

#If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:
#
#  apiVersion: v1
#  kind: Secret
#  metadata:
#    name: example-tls
#    namespace: foo
#  data:
#    tls.crt: <base64 encoded cert>
#    tls.key: <base64 encoded key>

kubectl get pods -n default

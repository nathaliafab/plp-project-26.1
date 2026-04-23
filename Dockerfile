FROM eclipse-temurin:25-jdk


RUN apt-get update \
	&& apt-get install -y --no-install-recommends maven \
	&& rm -rf /var/lib/apt/lists/* \
	&& useradd -ms /bin/bash plpuser



# Copia arquivos e ajusta permissões para o usuário não-root
COPY --chown=plpuser:plpuser . /workspace


WORKDIR /workspace/Imperativa1

# Troca para o usuário não-root
USER plpuser

CMD ["/bin/bash"]

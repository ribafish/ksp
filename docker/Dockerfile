FROM --platform=linux/amd64 bellsoft/liberica-openjdk-debian:11 AS openjdk11
FROM --platform=linux/amd64 adoptopenjdk/openjdk9:latest AS openjdk9

FROM --platform=linux/amd64 gradle/gradle-enterprise-test-distribution-agent:2.0.1

ENV JDK11=/opt/java/openjdk-11
COPY --from=openjdk11 /usr/lib/jvm/jdk ${JDK11}

ENV JDK9=/opt/java/openjdk-9
ENV JDK_9=${JDK9}
COPY --from=openjdk9 /opt/java/openjdk ${JDK9}

ENV TEST_DISTRIBUTION_AGENT_SERVER=https://ec2-3-237-201-221.compute-1.amazonaws.com
ENV TEST_DISTRIBUTION_AGENT_ALLOW_UNTRUSTED=true
ENV TEST_DISTRIBUTION_AGENT_CAPABILITIES=my-local-agent
ENV TEST_DISTRIBUTION_AGENT_API_KEY=cynkhriekpan6vqmfcrjfmkelhbvosxyjkvffeuehz6nung65t5a

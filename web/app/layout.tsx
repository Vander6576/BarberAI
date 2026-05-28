import React from 'react';
import './globals.css';

export const metadata = {
  title: 'BarberFlow - Agenda Inteligente',
  description: 'Gestão de Barbearias, comissionamentos via Pix e análise de encaixes por Inteligência Artificial.',
  manifest: '/manifest.json',
  viewport: 'width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no',
  themeColor: '#d9a05b'
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-BR" className="dark">
      <body className="bg-[#0d0e12] selection:bg-gold/35 selection:text-white antialiased">
        {children}
        
        {/* Simple Script to register the PWA Service Worker */}
        <script
          dangerouslySetInnerHTML={{
            __html: `
              if ('serviceWorker' in navigator) {
                window.addEventListener('load', function() {
                  navigator.serviceWorker.register('/sw.js').then(function(reg) {
                    console.log('ServiceWorker registradocomb sucesso: ', reg.scope);
                  }, function(err) {
                    console.log('Erro de registro ServiceWorker: ', err);
                  });
                });
              }
            `
          }}
        />
      </body>
    </html>
  );
}
